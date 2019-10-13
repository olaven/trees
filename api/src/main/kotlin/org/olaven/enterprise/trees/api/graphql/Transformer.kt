package org.olaven.enterprise.trees.api.graphql

import org.olaven.enterprise.trees.api.entity.LocationEntity
import org.olaven.enterprise.trees.api.entity.PlantEntity
import org.olaven.enterprise.trees.api.graphql.types.LocationType
import org.olaven.enterprise.trees.api.graphql.types.PlantType


/**
 * Transformer methods between
 * database entities and types sent
 * with GraphQL
 */
class Transformer {

    companion object {

        fun toPlantType(entity: PlantEntity) = PlantType(
                name = entity.name,
                description = entity.description,
                height = entity.height,
                age = entity.age,
                location = entity.location.id!!,
                id = entity.id
        )

        fun toLocationType(entity: LocationEntity) = LocationType(
                x = entity.x,
                y = entity.y,
                id = entity.id
        )
    }
}