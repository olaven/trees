package org.olaven.enterprise.trees.api.misc

import org.olaven.enterprise.trees.api.entity.LocationEntity
import org.olaven.enterprise.trees.api.repository.LocationRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.annotation.PostConstruct

@Service
@Profile(value = ["local"])
class DefaultDataInitializer(
        private val locationRepository: LocationRepository
) {

    @PostConstruct
    fun initialize() {

        val locations = getLocations()
        locationRepository.saveAll(locations)
    }

    //TODO: add plants on locations
    private fun getLocations(): List<LocationEntity> = listOf(
            LocationEntity(59.9838, 10.7256, emptyList(), timestamp = epochMilli()),
            LocationEntity(61.1488, 10.3743, emptyList(), timestamp = epochMilli()),
            LocationEntity(61.9216, 7.6694, emptyList(), timestamp = epochMilli()),
            LocationEntity(59.0539, 9.7275, emptyList(), timestamp = epochMilli())
    )

    private fun epochMilli() =
            ZonedDateTime.now()
                    .toInstant()
                    .toEpochMilli()
}