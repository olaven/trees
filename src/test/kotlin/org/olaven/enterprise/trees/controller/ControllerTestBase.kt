package org.olaven.enterprise.trees.controller

import com.github.javafaker.Faker
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.olaven.enterprise.trees.TestBase
import org.olaven.enterprise.trees.TreeApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ TreeApplication::class ], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class ControllerTestBase: TestBase() {

    @LocalServerPort
    protected var port = 0


    @BeforeEach
    fun init() {
        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.basePath = "api"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        databaseReset.reset();
    }
}