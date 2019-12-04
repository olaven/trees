package org.olaven.trees.api.controller

import io.swagger.annotations.*
import org.olaven.trees.api.annotations.IsLocation
import org.olaven.trees.api.dto.LocationDTO
import org.olaven.trees.api.dto.Page
import org.olaven.trees.api.dto.WrappedResponse
import org.olaven.trees.api.entity.LocationEntity
import org.olaven.trees.api.misc.CallCount
import org.olaven.trees.api.misc.HasCallCount
import org.olaven.trees.api.repository.LocationRepository
import org.olaven.trees.api.transformer.LocationTransformer
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.concurrent.TimeUnit

@RestController
@Api(value ="api/locations", description = "doing operations on locations")
@RequestMapping(value = ["api/locations"])
@Validated
class LocationController(

        val locationRepository: LocationRepository,
        val locationTransformer: LocationTransformer
): HasCallCount {

    enum class Expand {
        NONE, PLANTS
    }

    override val callCount = CallCount()

    @GetMapping("")
    @ApiOperation("Get locations based on a center")
    @ApiResponses(
            ApiResponse(code = 200, message = "A page of locations, relative to given center")
    )
    fun getLocationsCenter(
            @ApiParam("The lattitude of current center")
            @RequestParam("lat")
            lat: Double,
            @ApiParam("The logitude of current center")
            @RequestParam("long")
            long: Double,
            @ApiParam("Define extra data to fetch")
            @RequestParam("expand", required = false, defaultValue = "NONE")
            expand: Expand
    ): ResponseEntity<WrappedResponse<Page<LocationDTO>>> {

        callCount.getAll++
        val locations = locationRepository      //TODO: change value based on input / page next something
                .getNextCenterPage(15, lat, long, expand == Expand.PLANTS)
                .map { locationTransformer.toDTO(it, expand == Expand.PLANTS) }

        //TODO: next logic
        val next = null

        val page = Page(locations, next)

        return ResponseEntity
                .status(200)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .body(WrappedResponse(200, page))
    }

/*    @GetMapping("TEST_ABOVE_REPLACED_TEMP")
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
    }*/

    @GetMapping("/{id}")
    @ApiResponse(code = 200, message = "One specific location")
    @ApiOperation("Get a specific location")
    fun getLocation(
            @PathVariable(required = true)
            id: Long,
            @RequestParam(value = "expand", required = false, defaultValue = "NONE")
            expand: Expand
    ): ResponseEntity<WrappedResponse<LocationDTO>> {

        callCount.getOne++

        val entity = locationRepository.findById(id)

        return if (entity.isPresent) {

            okLocationResponse(entity.get(), expand == Expand.PLANTS)
        } else {

            ResponseEntity
                    .notFound()
                    .build()
        }
    }

    @GetMapping("/random")
    @ApiResponse(code = 307, message = "Temporary redirect to random location.")
    @ApiOperation(value = "Get redirected to a random location.")
    fun getRandomLocation(
            @RequestParam("expand", required = false, defaultValue = "NONE")
            expand: Expand
    ): ResponseEntity<WrappedResponse<LocationDTO>> {


        val entity = locationRepository.getRandom()
        val code = if (entity == null) 404 else 307
        val location = if (entity == null)
            ""
        else
            "${entity.id}?expand=${expand}"

        return ResponseEntity
                .status(code)
                .location(URI.create(location)) //TODO: avoid sending if 404?
                .body(WrappedResponse(
                    code
                ))
    }

    //TODO: remove this possibility?
    @PostMapping("")
    fun createLocation(
            @RequestBody
            @IsLocation
            dto: LocationDTO
    ): ResponseEntity<WrappedResponse<Nothing>> {

        val entity = locationRepository.save(locationTransformer.toEntity(dto))
        val location = URI.create("locations/${entity.id}")

        return ResponseEntity.created(location).body(WrappedResponse(
                code = 201, data = null
        ))
    }

    private fun okLocationResponse(entity: LocationEntity, includePlants: Boolean): ResponseEntity<WrappedResponse<LocationDTO>> {

        val dto = locationTransformer.toDTO(entity, includePlants)
        return ResponseEntity
                .status(200)
                .cacheControl(CacheControl.maxAge(5, TimeUnit.HOURS))
                .eTag(entity.version.toString())
                .lastModified(entity.timestamp)
                .body(WrappedResponse(
                        200, dto
                ))
    }
}