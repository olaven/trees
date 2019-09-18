package org.olaven.enterprise.trees.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import org.olaven.enterprise.trees.dto.LocationDTO
import org.olaven.enterprise.trees.dto.Page
import org.olaven.enterprise.trees.dto.WrappedResponse
import org.olaven.enterprise.trees.repository.LocationRepository
import org.olaven.enterprise.trees.transformer.LocationTransformer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@Api(value ="trees/api/locations", description = "doing operations on locations")
@RequestMapping(value = ["trees/api/locations"])
class LocationController {

    enum class Expand {
        NONE, PLANTS
    }

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

        val size = 5
        val entities = locationRepository.getNextPage(size, keysetId, expand == Expand.PLANTS)
        val locations = locationTransformer.toDTOs(entities)

        val next =
            if (locations.count() == size)
                "/locations?keysetId=${locations.last().id}"
            else null

        val page = Page(locations, next)
        return ResponseEntity.status(200).body(WrappedResponse(
                200,
                page
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