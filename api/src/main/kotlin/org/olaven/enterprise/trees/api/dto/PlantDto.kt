package org.olaven.enterprise.trees.api.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "Representing a plant")
data class PlantDto (
        @ApiModelProperty("The name of the plant")
        var name: String?,
        @ApiModelProperty("Description of the plant")
        var description: String?,
        @ApiModelProperty("Height in meters")
        var height: Double?,
        @ApiModelProperty("Years of age")
        var age: Int?,
        @ApiModelProperty("The location of the plant")
        var location: LocationDTO?,
        var id: Long? = null
): DTO()