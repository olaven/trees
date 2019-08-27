package org.olaven.enterprise.trees.controller

import com.github.javafaker.Faker
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.web.server.LocalServerPort

open class ControllerTestBase {

    @LocalServerPort
    protected var port = 0

    protected val faker = Faker()

    @BeforeEach
    fun init() {
        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }
}