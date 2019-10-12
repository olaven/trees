package org.olaven.enterprise.trees.misc

import com.github.javafaker.Faker
import org.olaven.enterprise.trees.entity.LocationEntity
import org.olaven.enterprise.trees.entity.PlantEntity
import org.olaven.enterprise.trees.repository.LocationRepository
import org.olaven.enterprise.trees.repository.PlantRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.annotation.PostConstruct

@Service
@Profile(value = ["local"])
class DefaultDataInitializer(
        private val locationRepository: LocationRepository,
        private val plantRepository: PlantRepository
) {

    private val faker = Faker()

    @PostConstruct
    fun initialize() {

        locationRepository.saveAll(getLocations())

        //NOTE: getting back locations _with correct_ ID, not default one
        val locations = locationRepository.findAll().toList()
        val plants = getPlants(locations)
        plantRepository.saveAll(plants)
    }

    //TODO: add plants on locations
    private fun getLocations(): List<LocationEntity> = listOf(
            LocationEntity(59.9838, 10.7256, emptyList(), timestamp = epochMilli()),
            LocationEntity(61.1488, 10.3743, emptyList(), timestamp = epochMilli()),
            LocationEntity(61.9216, 7.6694, emptyList(), timestamp = epochMilli()),
            LocationEntity(59.0539, 9.7275, emptyList(), timestamp = epochMilli())
    )

    private fun getPlants(locations: List<LocationEntity>): List<PlantEntity> = (0..20).map {
        PlantEntity(
                name = faker.funnyName().name(),
                description = faker.lorem().paragraph(),
                height = faker.random().nextDouble(),
                age = faker.random().nextInt(0, 2000),
                location = locations.random(),
                timestamp = ZonedDateTime.now().toInstant().toEpochMilli()
        )
    }

    private fun epochMilli() =
            ZonedDateTime.now()
                    .toInstant()
                    .toEpochMilli()
}