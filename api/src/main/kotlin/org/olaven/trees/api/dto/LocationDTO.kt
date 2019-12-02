package org.olaven.trees.api.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "Representing a location.")
class LocationDTO (

        @ApiModelProperty("X coordinate of location")
        var x: Double?,

        @ApiModelProperty("Y coordinate of location")
        var y: Double?,

        @ApiModelProperty("ID of location")
        val id: Long?,

        @ApiModelProperty("Plants on this location")
        val plants: List<PlantDto> = emptyList()
): DTO()