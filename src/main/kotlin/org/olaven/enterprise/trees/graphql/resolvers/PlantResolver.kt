package org.olaven.enterprise.trees.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import org.olaven.enterprise.trees.graphql.types.PlantType
import org.springframework.stereotype.Component

@Component
class PlantResolver
    : GraphQLResolver<PlantType> {

    fun getLocation(plant: PlantType) {


    }
}
