package org.olaven.enterprise.trees.graphql.types

class LocationType (
        var x: Double?,
        var y: Double?,
        val id: Long?
        //NOTE: entity in db keeps relation to plant
)