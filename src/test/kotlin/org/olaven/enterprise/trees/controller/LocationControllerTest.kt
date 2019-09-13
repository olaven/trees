package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class LocationControllerTest: ControllerTestBase() {


    @Test
    fun `database has none of this entity before tests run`() {

        getAll()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("list.size()", CoreMatchers.`is`(0))
    }

    @Test
    fun `can create a location`() {

        val dto = getLocationDTO()
        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post("/locations")
                .then()
                .statusCode(201)
                .body("code", equalTo(201))
                .body("data", equalTo(null))

    }

    @Test
    fun `can get a locations`() {

        val n = 5;
        persistLocations(n)

        getAll()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("list.size()", CoreMatchers.`is`(n))
    }


    @Test
    fun `get all-endpoint returns wrapped response`() {

        val location = getLocationDTO()
        persistLocation(location)

        val retrieved = getAll() //NOTE: object is in ".data"
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().jsonPath().getList<HashMap<String, Any>>("").first()

        assertEquals(200, retrieved["code"])
    }


    private fun persistLocations(n: Int) {

        (0 until n).forEach { _ ->

            val dto = getLocationDTO()
            persistLocation(dto)
        }
    }

    private fun getAll() = given()
            .contentType(ContentType.JSON)
            .get("/locations")
            .then()
}