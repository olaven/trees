package org.olaven.enterprise.trees.controller

import com.google.common.base.Throwables
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import org.olaven.enterprise.trees.dto.TreeDto
import org.olaven.enterprise.trees.entity.TreeEntity
import org.olaven.enterprise.trees.repository.TreeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.ConstraintViolationException

@RestController
class TreeController {

    @Autowired
    private lateinit var treeRepository: TreeRepository;

    @GetMapping("/trees")
    @ApiOperation("Get all trees")
    @ApiResponse(code = 200, message = "all trees")
    fun getTrees() = //TODO: returns weird data at: http://localhost:8080/trees/
            treeRepository.findAll()
                    .map { toDTO(it) }
                    .let { ResponseEntity.status(HttpStatus.OK).body(this) }

    @GetMapping("/trees/{id}")
    fun getTree(@PathVariable id: Long) =
            treeRepository.findById(id)
                .run {
                    this.get()
                }.run {
                    toDTO(this)
                }

    @PostMapping("trees", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation("Create a treeDto")
    @ApiResponse(code = 201, message = "The id of newly created tree")
    fun postTree(@RequestBody treeDto: TreeDto): ResponseEntity<Long> {

        if (treeDto.id != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build()
        }

        try {

            val entity = treeRepository.save(toEntity(treeDto))
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .location(URI.create("/tress/${entity.id}"))
                    .build()

        } catch (exception: Exception) {

            if (Throwables.getRootCause(exception) is ConstraintViolationException)
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
            else throw exception;
        }
    }

    private fun toEntity(dto: TreeDto): TreeEntity {

        class ConversionException: Exception();
        return if (dto.name == null  || dto.description == null ||
                dto.age == null || dto.height == null) throw ConversionException()
        else TreeEntity(dto.name, dto.description, dto.height, dto.age);
    }


    private fun toDTO(entity: TreeEntity) =
            TreeDto(
                    entity.name, entity.description,
                    entity.height, entity.age,
                    entity.id
            );

    private fun toDTOs(entities: List<TreeEntity>) =
            entities.map { toDTO(it) }
}