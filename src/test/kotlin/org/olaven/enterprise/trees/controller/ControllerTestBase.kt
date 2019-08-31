package org.olaven.enterprise.trees.controller

import com.github.javafaker.Faker
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.olaven.enterprise.trees.DatabaseReset
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort

open class ControllerTestBase {

    @LocalServerPort
    protected var port = 0

    @Autowired
    protected lateinit var databaseReset: DatabaseReset

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
}