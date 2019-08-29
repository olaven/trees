package org.olaven.enterprise.trees.controller

import com.google.common.base.Throwables
import io.swagger.annotations.*
import org.olaven.enterprise.trees.dto.PlantDto
import org.olaven.enterprise.trees.entity.PlantEntity
import org.olaven.enterprise.trees.repository.PlantRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.ConstraintViolationException

@RestController
@Api(value = "/plants", description = "doing operations on plants")
@RequestMapping(value = ["/plants"])
class PlantController {

    @Autowired
    private lateinit var plantRepository: PlantRepository;

    @GetMapping("/")
    @ApiOperation("Get all plants")
    @ApiResponse(code = 200, message = "all plants")
    fun getTrees() = //TODO: returns weird data at: http://localhost:8080/plants/
            plantRepository.findAll()
                    .map { toDTO(it) }
                    .let { ResponseEntity.status(HttpStatus.OK).body(this) }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a specific plant")
    @ApiResponse(code = 200, message = "the body of plant")
    fun getTree(@PathVariable id: Long): ResponseEntity<PlantDto> {

        val result = plantRepository.findById(id)
        return if (!result.isPresent)
            ResponseEntity.notFound().build()
        else ResponseEntity.ok().body(toDTO(result.get()))
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation("Create a plant")
    @ApiResponse(code = 201, message = "The id of newly created plant")
    fun postTree(@RequestBody plantDto: PlantDto): ResponseEntity<Long> {

        if (plantDto.id != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build()
        }

        try {

            val entity = plantRepository.save(toEntity(plantDto))
            val location = URI.create("plants/${entity.id}")

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .location(location)
                    .build()

        } catch (exception: Exception) {

            if (Throwables.getRootCause(exception) is ConstraintViolationException)
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
            else throw exception;
        }
    }

    @PutMapping(value = ["/{id}"], consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE])
    @ApiResponses(value = [
        ApiResponse(code = 204, message = "existing plant updated"),
        ApiResponse(code = 409, message = "id mismatch between body and path")
    ])
    @ApiOperation("Replace a plant with new value")
    fun updatePlant(
            @ApiParam(value = "plants's id") @PathVariable id: Long,
            @ApiParam(value = "the new plant") @RequestBody dto: PlantDto
    ): ResponseEntity<PlantDto> {

        if (id != dto.id) return ResponseEntity.status(HttpStatus.CONFLICT).build()

        return if (
            dto.name == null ||
            dto.description == null ||
            dto.age == null ||
            dto.height == null
        ) ResponseEntity.badRequest().build()

        else {

            return if (plantRepository.existsById(id)) {

                plantRepository.update(id, dto.name!!, dto.description, dto.age, dto.height)
                ResponseEntity.noContent().build()
            } else {

                ResponseEntity.badRequest().build()
            }
        }
    }

    private fun toEntity(dto: PlantDto): PlantEntity {

        class ConversionException: Exception();
        return if (dto.name == null  || dto.description == null ||
                dto.age == null || dto.height == null) throw ConversionException()
        else PlantEntity(dto.name!!, dto.description, dto.height, dto.age);
    }


    private fun toDTO(entity: PlantEntity) =
            PlantDto(
                    entity.name, entity.description,
                    entity.height, entity.age,
                    entity.id
            );

    private fun toDTOs(entities: List<PlantEntity>) =
            entities.map { toDTO(it) }
}