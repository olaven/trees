package org.olaven.enterprise.trees.controller

import com.github.javafaker.Faker
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.olaven.enterprise.trees.DatabaseReset
import org.olaven.enterprise.trees.TreeApplication
import org.olaven.enterprise.trees.dto.LocationDTO
import org.olaven.enterprise.trees.dto.PlantDto
import org.olaven.enterprise.trees.entity.LocationEntity
import org.olaven.enterprise.trees.repository.LocationRepository
import org.olaven.enterprise.trees.repository.PlantRepository
import org.olaven.enterprise.trees.transformer.LocationTransformer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ TreeApplication::class ], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class ControllerTestBase {

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var databaseReset: DatabaseReset
    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var plantRepository: PlantRepository
    @Autowired
    private lateinit var locationTransformer: LocationTransformer

    protected val faker = Faker()

    @BeforeEach
    fun init() {
        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.basePath = "trees/api"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        databaseReset.reset();
    }

    @Test
    abstract fun `database has none of this entity before tests run`()

    protected fun persistLocation(dto: LocationDTO): LocationEntity {


        val entity = locationTransformer.toEntity(dto)
        return locationRepository.save(entity)
    }

    protected fun getLocationDTO(): LocationDTO {

        val x = faker.random().nextDouble()
        val y = faker.random().nextDouble()

        return LocationDTO(x, y, null)
    }

    protected fun getPlantDTO(location: LocationDTO? = null): PlantDto {

        val name = faker.funnyName().name()
        val description = faker.lorem().paragraph()
        val height = faker.number().randomDouble(2, 1, 100)
        val age = faker.number().numberBetween(4, 20)

        val actualLocation = location ?: getLocationDTO()
        val location = locationTransformer.toDTO(persistLocation(actualLocation))
        return PlantDto(name, description, height, age, location)
    }
}