package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class LocationControllerTest: ControllerTestBase() {

    @Test
    fun `can get a locations`() {

        val n = 5;
        (0 until n).forEach { _ ->

            val dto = getLocationDTO()
            persistLocation(dto)
        }

        get()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("list.size()", CoreMatchers.`is`(n))
    }

    private fun get() = given()
            .contentType(ContentType.JSON)
            .get("/locations")
            .then()
}