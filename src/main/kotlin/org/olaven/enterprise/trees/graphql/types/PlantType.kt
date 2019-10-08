package org.olaven.enterprise.trees.graphql.types

class PlantType (
    var name: String,
    var description: String,
    var height: Double,
    var age: Int,
    var location: Long,
    var id: Long? = null
)