package org.olaven.trees.api

import com.github.javafaker.Faker
import org.junit.jupiter.api.extension.ExtendWith
import org.olaven.trees.api.dto.LocationDTO
import org.olaven.trees.api.dto.PlantDto
import org.olaven.trees.api.entity.LocationEntity
import org.olaven.trees.api.entity.PlantEntity
import org.olaven.trees.api.repository.LocationRepository
import org.olaven.trees.api.repository.PlantRepository
import org.olaven.trees.api.transformer.LocationTransformer
import org.olaven.trees.api.transformer.PlantTransformer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest
@ExtendWith(SpringExtension::class)
@DirtiesContext // new application for every test (cache is happy)
@Testcontainers
// @ContextConfiguration(initializers = [TestBase.Companion.Initializer::class])
class TestBase {

/*
    companion object {

        *//*
            workaround to current Kotlin (and other JVM languages) limitation
            see https://github.com/testcontainers/testcontainers-java/issues/318
         *//*                     // TODO: test kartoza/postgis with password and username docker if this does not work
        class KPsqlContainer: PostgreSQLContainer<KPsqlContainer>("kartoza/postgis")
        //class KPsqlContainer : PostgreSQLContainer<KPsqlContainer>("mdillon/postgis")

        @ClassRule
        @JvmField
        val dockerPostgres = KPsqlContainer()
                .withExposedPorts(5432)
                .withUsername("docker")
                .withPassword("docker")

        class Initializer: ApplicationContextInitializer<ConfigurableApplicationContext> {

            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext?) {
                val values = TestPropertyValues.of(
                        "spring.datasource.url=" + dockerPostgres.jdbcUrl,
                        "spring.datasource.password=" + dockerPostgres.password,
                        "spring.datasource.username=" + dockerPostgres.username
                )
                values.applyTo(configurableApplicationContext)
            }
        }

        *//*
        * url: "jdbc:tc:postgresql://postgres_movies:5432/postgres"
            username: "docker"
            password: "docker"
            driver-class-name: "org.testcontainers.jdbc.ContainerDatabaseDriver"
        * *//*
    }*/

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