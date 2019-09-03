package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test

internal class LocationControllerTest: ControllerTestBase() {


    @Test
    override fun `database has none of this entity before tests run`() {

        getAll()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("list.size()", CoreMatchers.`is`(0))
    }

    @Test
    fun `can get a locations`() {

        val n = 5;
        (0 until n).forEach { _ ->

            val dto = getLocationDTO()
            persistLocation(dto)
        }

        getAll()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("list.size()", CoreMatchers.`is`(n))
    }

    private fun getAll() = given()
            .contentType(ContentType.JSON)
            .get("/locations")
            .then()
}