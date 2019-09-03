package org.olaven.enterprise.trees.dto

data class PlantDto (
    var name: String?,
    val description: String?,
    val height: Double?,
    val age: Int?,
    val location: LocationDTO?,
    var id: Long? = null
)