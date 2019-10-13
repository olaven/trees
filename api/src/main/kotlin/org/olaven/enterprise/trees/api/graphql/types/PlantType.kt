package org.olaven.enterprise.trees.api.graphql.types

class PlantType (
    var name: String,
    var description: String,
    var height: Double,
    var age: Int,
    var location: Long,
    var id: Long? = null
)

class InputPlant (
    var name: String,
    var description: String,
    var height: Double,
    var age: Int,
    var location: Long, // ID of location
    var id: Long? = null
)