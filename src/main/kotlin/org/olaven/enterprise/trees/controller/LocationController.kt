package org.olaven.enterprise.trees.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import org.olaven.enterprise.trees.CallCount
import org.olaven.enterprise.trees.HasCallCount
import org.olaven.enterprise.trees.dto.LocationDTO
import org.olaven.enterprise.trees.dto.Page
import org.olaven.enterprise.trees.dto.WrappedResponse
import org.olaven.enterprise.trees.repository.LocationRepository
import org.olaven.enterprise.trees.transformer.LocationTransformer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.concurrent.TimeUnit

@RestController
@Api(value ="api/locations", description = "doing operations on locations")
@RequestMapping(value = ["api/locations"])
class LocationController: HasCallCount {

    enum class Expand {
        NONE, PLANTS
    }

    override val callCount = CallCount()

    @Autowired
    private lateinit var locationRepository: LocationRepository
    @Autowired
    private lateinit var locationTransformer: LocationTransformer

    @GetMapping("")
    @ApiResponse(code = 200, message = "All locations")
    @ApiOperation(value = "Get all locations")
    fun getLocations(
            @RequestParam("keysetId", required = false)
            keysetId: Long?,
            @RequestParam("expand", required = false, defaultValue = "NONE")
            expand: Expand
    ): ResponseEntity<WrappedResponse<Page<LocationDTO>>> {

        callCount.getAll++
        val size = 5
        val entities = locationRepository.getNextPage(size, keysetId, expand == Expand.PLANTS)
        val locations = entities.map { locationTransformer.toDTO(it, expand == Expand.PLANTS) }

        val next =
            if (locations.count() == size)
                "/locations?keysetId=${locations.last().id}"
            else null

        val page = Page(locations, next)
        return ResponseEntity
                .status(200)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .body(WrappedResponse(
                200,
                page
        ))
    }

    @GetMapping("/{id}")
    @ApiResponse(code = 200, message = "One specific location")
    @ApiOperation("Get a specific location")
    fun getLocation(
            @PathVariable(required = true)
            id: Long,
            @RequestParam(value = "expand", required = false, defaultValue = "NONE")
            expand: Expand
    ): ResponseEntity<WrappedResponse<LocationDTO>> {

        val entity = locationRepository.findById(id)
        val dto = if (entity.isPresent)
            locationTransformer.toDTO(entity.get(), expand == Expand.PLANTS)
        else null
        val code = if (dto == null) 404 else 200

        return ResponseEntity.ok(WrappedResponse(
                code, dto
        ))
    }

    @GetMapping("/random")
    @ApiResponse(code = 200, message = "Temporary redirect to random location.")
    @ApiOperation(value = "Get redirected to a random location.")
    fun getRandomLocation(
            @RequestParam("expand", required = false, defaultValue = "NONE")
            expand: Expand
    ): ResponseEntity<WrappedResponse<LocationDTO>> {


        val entity = locationRepository.getRandom()
        val status = if (entity == null) 404 else 307
        val location = if (entity == null)
            ""
        else
            "${entity.id}?expand=${expand}"

        return ResponseEntity
                .status(status)
                .location(URI.create(location)) //TODO: avoid sending if 404?
                .body(WrappedResponse(
                status
        ))
    }

    //TODO: remove this possibility?
    @PostMapping("")
    fun createLocation(@RequestBody dto: LocationDTO): ResponseEntity<WrappedResponse<Nothing>> {

        val entity = locationRepository.save(locationTransformer.toEntity(dto));
        val location = URI.create("locations/${entity.id}")

        return ResponseEntity.created(location).body(WrappedResponse(
                code = 201, data = null
        ))
    }
}