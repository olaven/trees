package org.olaven.enterprise.trees.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.olaven.enterprise.trees.repository.LocationRepository
import org.olaven.enterprise.trees.transformer.LocationTransformer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(value ="trees/api/locations", description = "getting relevant locations")
@RequestMapping(value = ["trees/api/plants"])
class LocationController {

    @Autowired
    private lateinit var locationRepository: LocationRepository
    @Autowired
    private lateinit var locationTransformer: LocationTransformer

    @GetMapping() //TODO: should use pagination
    @ApiResponse(code = 200, message = "All locations")
    fun getLocations() =
            locationRepository.findAll()
                    .map { locationTransformer.toDTO(it) }
                    .let { ResponseEntity.ok(it) }
}