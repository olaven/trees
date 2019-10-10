package org.olaven.enterprise.trees.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import org.olaven.enterprise.trees.graphql.Transformer
import org.olaven.enterprise.trees.graphql.types.LocationType
import org.olaven.enterprise.trees.graphql.types.PlantType
import org.olaven.enterprise.trees.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class PlantResolver(
    private val locationRepository: LocationRepository
) : GraphQLResolver<PlantType> {

    fun getLocation(plant: PlantType): LocationType? {

        //TODO: refactor, as this shares a lot of code with QueryResolver.location
        val id = plant.location
        val optional = locationRepository.findById(id)
        return if (optional.isPresent)
            Transformer.toLocationType(optional.get())
        else null
    }
}
