package org.olaven.enterprise.trees.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.olaven.enterprise.trees.graphql.Transformer
import org.olaven.enterprise.trees.graphql.types.LocationType
import org.olaven.enterprise.trees.graphql.types.PlantType
import org.olaven.enterprise.trees.repository.LocationRepository
import org.olaven.enterprise.trees.repository.PlantRepository
import org.springframework.stereotype.Component

@Component
class QueryResolver(
        private val locationRepository: LocationRepository,
        private val plantRepository: PlantRepository
): GraphQLQueryResolver {

    fun plant(id: Long): PlantType? {

        val optional = plantRepository.findById(id)
        return if (optional.isPresent)
            Transformer.toPlantType(optional.get())
        else null
    }

    fun plants(): Iterable<PlantType> = plantRepository.findAll()
            .map { Transformer.toPlantType(it) }


    fun location(id: Long): LocationType? {

        val optional = locationRepository.findById(id)
        return if (optional.isPresent)
            Transformer.toLocationType(optional.get())
        else null
    }

    fun locations(): Iterable<LocationType> = locationRepository.findAll()
            .map { Transformer.toLocationType(it) }
}