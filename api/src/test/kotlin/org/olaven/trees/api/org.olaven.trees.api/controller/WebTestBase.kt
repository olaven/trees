package org.olaven.trees.api.controller

import io.restassured.RestAssured
import org.junit.ClassRule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.olaven.trees.api.DatabaseReset
import org.olaven.trees.api.TestBase
import org.olaven.trees.api.TreeApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.Cache
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ TreeApplication::class ], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
abstract class WebTestBase(
        private val excludeBasePath: Boolean = false
): TestBase() {

    companion object {

        /*
            workaround to current Kotlin (and other JVM languages) limitation
            see https://github.com/testcontainers/testcontainers-java/issues/318
         */
        class KPsqlContainer : PostgreSQLContainer<KPsqlContainer>()

        @ClassRule
        @JvmField
        val postgres = KPsqlContainer()
                .withExposedPorts(5432)
    }



    @Autowired
    private lateinit var databaseReset: DatabaseReset

    @LocalServerPort
    protected var port = 0

    @Value("#{cacheManager.getCache('httpClient')}")
    private lateinit var httpClientCache: Cache


    @BeforeEach
    fun init() {

        databaseReset.reset()
        httpClientCache.clear()

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.basePath = if (excludeBasePath) "" else "api"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }
}