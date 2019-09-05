package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class WelcomeControllerTest: ControllerTestBase() {

    @Test
    fun `welcome message defaults to english`() {

        val message = given()
                .get("/")
                .then()
                .statusCode(200)
                .extract()
                .asString()

        assertTrue {
            message.contains("Welcome")
        }
    }


    @Test
    fun `responds in Norwegian if explicitly asked to`() {

        val message = given()
                .header("Accept-Language", "no")
                .get("/")
                .then()
                .statusCode(200)
                .extract()
                .asString()

        assertTrue {
            message.contains("Velkommen")
        }
    }
}