package org.olaven.enterprise.trees.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.olaven.enterprise.trees.annotations.IsLattitude
import org.olaven.enterprise.trees.annotations.IsLongitude

@ApiModel(description = "Representing a location.")
class LocationDTO (

        @ApiModelProperty("X coordinate of location")
        @IsLattitude
        var x: Double?,

        @ApiModelProperty("Y coordinate of location")
        @IsLongitude
        var y: Double?,

        @ApiModelProperty("ID of location")
        val id: Long?,

        @ApiModelProperty("Plants on this location")
        val plants: List<PlantDto> = emptyList()
): DTO()