package org.olaven.trees.api.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Throwables
import io.swagger.annotations.*
import org.olaven.trees.api.dto.LocationDTO
import org.olaven.trees.api.dto.PlantDto
import org.olaven.trees.api.dto.WrappedResponse
import org.olaven.trees.api.misc.CallCount
import org.olaven.trees.api.misc.HasCallCount
import org.olaven.trees.api.repository.PlantRepository
import org.olaven.trees.api.transformer.LocationTransformer
import org.olaven.trees.api.transformer.PlantTransformer
import org.springframework.http.CacheControl
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.concurrent.TimeUnit

@RestController
@Api(value ="api/plants", description = "doing operations on plants")
@RequestMapping(value = ["api/plants"])
class PlantController(
        private val plantRepository: PlantRepository,
        private val plantTransformer: PlantTransformer,
        private val locationTransformer: LocationTransformer
): HasCallCount {

    override val callCount = CallCount()

    //TODO: use pagination
    @GetMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation("Get all plants")
    @ApiResponse(code = 200, message = "All plants")
    fun getTrees(): ResponseEntity<List<WrappedResponse<PlantDto>>> =
            plantRepository.findAll()
                    .map { plantTransformer.toDTO(it) }
                    .map { WrappedResponse(200, it) }
                    .let { ResponseEntity.status(HttpStatus.OK).body(it) }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a specific plant")
    @ApiResponse(code = 200, message = "the body of plant")
    fun getTree(
            @PathVariable id: Long,
            @RequestHeader(value = "If-Match", required = false) ifMatch: String?,
            @RequestHeader(value = "If-Modified-Since", required = false) ifModifiedSince: String?
    ): ResponseEntity<WrappedResponse<PlantDto?>> {

        callCount.getOne++
        val result = plantRepository.findById(id)
        return if (!result.isPresent)
            ResponseEntity.status(404).body(WrappedResponse<PlantDto?>(
                    404, null
            ))
        else {

            val entity = result.get()
            val version = entity.version.toString()
            if (ifMatch != null && ifMatch != version)
                return ResponseEntity.status(304).build()

            val dto = plantTransformer.toDTO(entity)

            ResponseEntity
                .status(200)
                .cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
                .eTag(version)
                .lastModified(entity.timestamp)
                .body(WrappedResponse<PlantDto?>(
                    200, dto
                ))
        }
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation("Create a plant")
    @ApiResponse(code = 201, message = "The id of newly created plant")
    fun postPlant(@RequestBody plantDto: PlantDto): ResponseEntity<WrappedResponse<PlantDto?>> {

        if (plantDto.id != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(WrappedResponse<PlantDto?>(
                            409, null
                    ))

        }

        try {

            val entity = plantRepository.save(plantTransformer.toEntity(plantDto))
            val location = URI.create("plants/${entity.id}")

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .location(location)
                    .body(WrappedResponse<PlantDto?>(
                            201, plantTransformer.toDTO(entity)
                    ))
        } catch (exception: Exception) {

            //NOTE: I want to rethrow `ConstraintValidationException`, not Springs wrapper
            val cause = Throwables.getRootCause(exception)
            throw cause
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
            @ApiParam(value = "the new plant") @RequestBody dto: PlantDto,
            @RequestHeader(value = "If-None-Match", required = false) noneMatch: String?
    ): ResponseEntity<WrappedResponse<Nothing>> {

        if (id != dto.id) return ResponseEntity.status(HttpStatus.CONFLICT).build()

        return if (
            dto.name == null ||
            dto.description == null ||
            dto.age == null ||
            dto.height == null ||
            dto.location == null
        ) ResponseEntity.status(400).body(WrappedResponse(
                404, null, "The plant has null values." //TODO: more detailed
        ))

        else {

            return if (plantRepository.existsById(id)) {

                val oldEntity = plantRepository.findById(id).get()
                val version = oldEntity.version.toString()
                if (noneMatch != null && noneMatch == version) {
                    return ResponseEntity.status(412).build()
                }

                val location = locationTransformer.toEntity(dto.location!!)
                plantRepository.update(id, dto.name!!, dto.description!!, dto.age!!, dto.height!!, location)
                ResponseEntity.status(204).body(WrappedResponse(
                        204, null
                ))
            } else {

                ResponseEntity.status(404).body(WrappedResponse(
                        404, null, "The plant is not found"
                ))
            }
        }
    }

    @ApiOperation("Update the plant using JSON merge-patch")
    @PatchMapping(path = ["/{id}"], consumes = ["application/merge-patch+json"])
    fun updatePlant(
            @ApiParam("The unique ID of the plant")
            @PathVariable("id", required = true)
            id: Long,
            @ApiParam("The partial JSON patch")
            @RequestBody(required = true)
            jsonPatch: String
    ): ResponseEntity<WrappedResponse<Nothing>> {

        val entity = plantRepository.findById(id).orElse(null) ?:
            return ResponseEntity.notFound().build()
        val dto = plantTransformer.toDTO(entity)
        val jackson = ObjectMapper()
        val jsonNode: JsonNode

        try {
            jsonNode = jackson.readValue(jsonPatch, JsonNode::class.java)

        } catch (exception: Exception) {

            return ResponseEntity.status(400).body(WrappedResponse(
                    400, null
            ))
        }

        val newName: String?
        val newDescription: String?
        val newHeight: Double?
        val newAge: Int?
        val newLocation: LocationDTO?

        try {

            newName = getProperty(jsonNode, "name", {it.isTextual}, {it.asText()})?: dto.name
            newDescription = getProperty(jsonNode, "description", {it.isTextual}, {it.asText()})?: dto.description
            newHeight = getProperty(jsonNode, "height", {it.isDouble}, {it.asDouble()})?: dto.height
            newAge = getProperty(jsonNode, "age", {it.isInt}, {it.asInt()})?: dto.age
            newLocation = getLocationProperty(jsonNode)?: dto.location

        } catch (exception: IllegalArgumentException) {

            return ResponseEntity.status(400).body(WrappedResponse(
                    400, null
            ))
        }

        val locationEntity = locationTransformer.toEntity(newLocation!!)
        plantRepository.update(entity.id!!, newName!!, newDescription!!, newAge!!, newHeight!!, locationEntity)

        return ResponseEntity.status(201).body(WrappedResponse(
                201, null
        ))
    }

    private fun getLocationProperty(baseNode: JsonNode): LocationDTO? {

        val node = baseNode.get("location")
        if (node.isNull) return null

        val x = getProperty(node, "x", {it.isDouble}, {it.asDouble() })
        val y = getProperty(node ,"y", {it.isDouble}, {it.asDouble()})
        val id = getProperty(node, "id", {it.isLong}, {it.asLong()})

        return LocationDTO(x, y, id);
    }

    private fun<T> getProperty(
            baseNode: JsonNode,
            identifier: String,
            isValid: (node: JsonNode) -> Boolean,
            convert: (node: JsonNode) -> T
    ): T? {

        val node = baseNode.get(identifier)
        return when {
            node.isNull -> null
            isValid(node) -> convert(node)
            else -> throw IllegalArgumentException("$identifier was invalid.")
        }
    }
}

