package org.olaven.enterprise.trees.integration_tests

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.awaitility.Awaitility
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.greaterThan
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.concurrent.TimeUnit

/**
 * Created by arcuri82 on 04-Oct-19.
 */
@Testcontainers
class GatewayRestIT : GatewayIntegrationDockerTestBase() {

    companion object {
        init {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }
    }



    @Test
    fun `can get some locations`() {

        //NOTE: test assumes that some locations are added by default.
        given().get("/api/locations")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
    }

    @Test
    fun `can get some plants`() {

        given().get("/api/plants")
                .then()
                .statusCode(200)
    }

    @Test
    fun `can load frontend`() {

        given().get("")
                .then()
                .statusCode(200)
                .body(containsString("mapbox"))
    }

    @Test @Disabled
    fun testLoadBalance() {


        Awaitility.await().atMost(120, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until{

                    //TODO: something that works for my application
                    val messages = given().get("/service/messages")
                            .then()
                            .statusCode(200)
                            .extract().body().jsonPath().getList("system", String::class.java)

                    assertEquals(3, messages.toSet().size)
                    assertTrue(messages.contains("A"))
                    assertTrue(messages.contains("B"))
                    assertTrue(messages.contains("C"))

                    true
                }
    }
}