package org.olaven.enterprise.trees.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import org.olaven.enterprise.trees.graphql.types.LocationType
import org.springframework.stereotype.Component

@Component
class LocationResolver: GraphQLResolver<LocationType> {

    //TODO: I think I need the same methods as in schema
}