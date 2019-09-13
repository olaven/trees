package org.olaven.enterprise.trees.dto

data class PlantDto (
        var name: String?,
        var description: String?,
        var height: Double?,
        var age: Int?,
        var location: LocationDTO?,
        var id: Long? = null
): DTO()