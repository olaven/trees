package org.olaven.enterprise.trees

import com.github.javafaker.Faker
import org.junit.jupiter.api.extension.ExtendWith
import org.olaven.enterprise.trees.dto.LocationDTO
import org.olaven.enterprise.trees.dto.PlantDto
import org.olaven.enterprise.trees.entity.LocationEntity
import org.olaven.enterprise.trees.entity.PlantEntity
import org.olaven.enterprise.trees.repository.LocationRepository
import org.olaven.enterprise.trees.repository.PlantRepository
import org.olaven.enterprise.trees.transformer.LocationTransformer
import org.olaven.enterprise.trees.transformer.PlantTransformer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
class TestBase() {

    @Autowired
    private lateinit var locationTransformer: LocationTransformer
    @Autowired
    private lateinit var plantTransformer: PlantTransformer

    @Autowired
    protected lateinit var locationRepository: LocationRepository
    @Autowired
    protected lateinit var plantRepository: PlantRepository

    private val faker = Faker()

    protected fun persistLocation(dto: LocationDTO): LocationEntity {

        val entity = locationTransformer.toEntity(dto)
        return locationRepository.save(entity)
    }

    protected fun persistPlant(dto: PlantDto): PlantEntity {

        val entity = plantTransformer.toEntity(dto)
        return plantRepository.save(entity)
    }

    protected fun getLocationDTO(): LocationDTO {

        val x = faker.random().nextDouble()
        val y = faker.random().nextDouble()

        return LocationDTO(x, y, null)
    }

    protected fun getPlantDTO(location: LocationEntity? = null, includePlants: Boolean = false): PlantDto {

        val name = faker.funnyName().name()
        val description = faker.lorem().paragraph()
        val height = faker.number().randomDouble(2, 1, 100)
        val age = faker.number().numberBetween(4, 20)

        val actualLocation = location ?: persistLocation(getLocationDTO())
        val locationDTO = locationTransformer.toDTO(actualLocation, includePlants)

        return PlantDto(name, description, height, age, locationDTO)
    }

}