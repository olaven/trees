package org.olaven.enterprise.trees.api.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.google.common.base.Throwables
import graphql.execution.DataFetcherResult
import graphql.servlet.GenericGraphQLError
import org.olaven.enterprise.trees.api.entity.LocationEntity
import org.olaven.enterprise.trees.api.entity.PlantEntity
import org.olaven.enterprise.trees.api.graphql.types.InputLocation
import org.olaven.enterprise.trees.api.graphql.types.InputPlant
import org.olaven.enterprise.trees.api.repository.LocationRepository
import org.olaven.enterprise.trees.api.repository.PlantRepository
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import javax.validation.ConstraintViolationException

@Component
class MutationResolver(
        private val plantRepository: PlantRepository,
        private val locationRepository: LocationRepository
): GraphQLMutationResolver {

    fun createPlant(plant: InputPlant): DataFetcherResult<Long> {

        val location = locationRepository.findById(plant.location)
        if (!location.isPresent) {
            return DataFetcherResult<Long>(null, listOf(GenericGraphQLError("Given location does not exist.")))
        }

        return handledPersisting {

            val entity = PlantEntity(plant.name, plant.description, plant.height, plant.age, location.get(), timestamp = ZonedDateTime.now().toInstant().toEpochMilli())
            val persisted = plantRepository.save(entity)
            DataFetcherResult(persisted.id, listOf())
        }
    }

    fun updatePlant(id: Long, plant: InputPlant): DataFetcherResult<Boolean> {

        val plantOptional = plantRepository.findById(id)
        val locationOptional = locationRepository.findById(plant.location)

        return if (plantOptional.isPresent and locationOptional.isPresent) {

            plantRepository.update(id, plant.name, plant.description, plant.age, plant.height, locationOptional.get())
            DataFetcherResult(true, listOf())
        } else {

            DataFetcherResult(false, listOf(GenericGraphQLError("Invalid ID(s) -> plant or location not found")))
        }
    }

    fun createLocation(location: InputLocation): DataFetcherResult<Long> {

        return handledPersisting {

            val entity = LocationEntity(location.x, location.y, timestamp = ZonedDateTime.now().toInstant().toEpochMilli())
            val persisted = locationRepository.save(entity)
            DataFetcherResult(persisted.id!!, listOf())
        }
    }

    fun updateLocation(id: Long, location: InputLocation): DataFetcherResult<Boolean> {

        val locationOptional = locationRepository.findById(id)

        return if (locationOptional.isPresent) {

            locationRepository.update(id, location.x, location.y)
            DataFetcherResult(true, listOf())
        } else {

            DataFetcherResult(false, listOf(GenericGraphQLError("Invalid ID(s) -> plant or location not found")))
        }
    }


    //TODO: This error returns to much information about internal implementation. Message should avoid implementation details if possible. 
    private fun handledPersisting(action: () -> DataFetcherResult<Long>): DataFetcherResult<Long> {

        return try {

            action()
        } catch (exception: Exception) {

            val cause = Throwables.getRootCause(exception)
            val message = if (cause is ConstraintViolationException) {
                "Violated constraints: ${cause.message}"
            }else {
                "${exception.javaClass}: ${exception.message}"
            }

            DataFetcherResult<Long>(null, listOf(GenericGraphQLError(message)))
        }
    }
}