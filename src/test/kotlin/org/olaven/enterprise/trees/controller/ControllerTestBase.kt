package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.web.server.LocalServerPort

open class ControllerTestBase {

    @LocalServerPort
    protected var port = 0

    @BeforeEach
    fun init() {
        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }
}