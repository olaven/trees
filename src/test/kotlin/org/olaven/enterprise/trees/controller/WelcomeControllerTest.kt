package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import org.olaven.enterprise.trees.WebTestBase
import kotlin.test.assertTrue

internal class WelcomeControllerTest: WebTestBase() {

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
                .extract()
                .asString()

        assertTrue {
            message.contains("Velkommen")
        }
    }


    @Test
    fun `returns plaintext and UTF-8 by default`() {

        given()
                .get("/")
                .then()
                .contentType("text/plain;charset=UTF-8")
    }


    @Test
    fun `returns plaintext and  iso -8859-1 when explicitly asked to`() {

        given()
                .header("Accept", "text/plain;charset=ISO-8859-1")
                .get("/")
                .then()
                .contentType("text/plain;charset=ISO-8859-1")
    }


    @Test
    fun `Charset error`() {

        val norwegian = "øy" //UTF-16
        val iso = norwegian.toByteArray(charset("ISO-8859-1"))
        val utf = String(iso, charset("UTF-8"))

        assertTrue { utf.contains("�") }
    }
}
