package org.olaven.enterprise.trees.api.controller

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.olaven.enterprise.trees.api.DatabaseReset
import org.olaven.enterprise.trees.api.TestBase
import org.olaven.enterprise.trees.api.TreeApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.Cache
import org.springframework.test.context.junit.jupiter.SpringExtension



@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ TreeApplication::class ], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class WebTestBase(
        private val excludeBasePath: Boolean = false
): TestBase() {

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