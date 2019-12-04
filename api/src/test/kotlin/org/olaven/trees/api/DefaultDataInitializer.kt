package org.olaven.trees.api

import com.github.javafaker.Faker
import org.olaven.trees.api.entity.LocationEntity
import org.olaven.trees.api.entity.PlantEntity
import org.olaven.trees.api.repository.LocationRepository
import org.olaven.trees.api.repository.PlantRepository
import org.springframework.context.annotation.Profile
import org.springframework.data.geo.Point
import org.springframework.stereotype.Service
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

        val locations = getLocations()
        val plants = getPlants(locations)

        locationRepository.saveAll(locations)
        plantRepository.saveAll(plants)
    }

    private fun getLocations(): List<LocationEntity> = (0 until 100)
            .map {
                LocationEntity(
                        Point(
                                faker.number().randomDouble(8, 0, 90),
                                faker.number().randomDouble(8, -180, 180))
                        )
            }
            .toList()

    private fun getPlants(locations: List<LocationEntity>) = (0 until 125)
            .map {
                PlantEntity(
                        name = faker.beer().name(),
                        description = faker.lorem().paragraph(),
                        height = faker.number().randomDouble(2, 1, 55),
                        age = faker.number().numberBetween(0, 250),
                        location = locations.random())
            }
            .toList()
}