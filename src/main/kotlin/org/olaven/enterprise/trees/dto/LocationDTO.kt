package org.olaven.enterprise.trees.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "Representing a location.")
class LocationDTO (

        @ApiModelProperty("X coordinate of location")
        val x: Double?,
        @ApiModelProperty("Y coordinate of location")
        val y: Double?,
        @ApiModelProperty("ID of location")
        val id: Long?,
        @ApiModelProperty("Plants on this location")
        val plants: List<PlantDto> = emptyList()
): DTO()