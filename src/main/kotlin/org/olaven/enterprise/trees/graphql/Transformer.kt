package org.olaven.enterprise.trees.graphql

import org.olaven.enterprise.trees.entity.LocationEntity
import org.olaven.enterprise.trees.entity.PlantEntity
import org.olaven.enterprise.trees.graphql.types.LocationType
import org.olaven.enterprise.trees.graphql.types.PlantType
import org.olaven.enterprise.trees.repository.LocationRepository

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