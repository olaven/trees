package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.olaven.enterprise.trees.TreeApplication
import org.olaven.enterprise.trees.dto.PlantDto
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

internal class PlantControllerTest: ControllerTestBase() {

    @Test
    fun `can create a plant`() {

        val dto = getPlantDTO()
        val id = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post("plants/")
                .then()
                .statusCode(201)
                .extract().asString();

        assertNotNull(id);
    }

    @Test
    fun `the database is clean before tests`() {

        getAll()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("list.size()", CoreMatchers.`is`(0))
    }

    @Test
    fun `can create and retrieve a plant`() {

        val dto = getPlantDTO()
        val location = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post("plants/")
                .then()
                .statusCode(201)
                .extract().header("Location")


        val retrieved = given()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .`as`(PlantDto::class.java)

        assertEquals(dto.name, retrieved.name);
        assertEquals(dto.description, retrieved.description);
        assertEquals(dto.age, retrieved.age);
    }

    @Test
    fun `can retrieve all plants`() {

        val n = 5;
        (0 until n).forEach { _ ->
            post(getPlantDTO())
        }

        getAll()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("list.size()", CoreMatchers.`is`(n))
    }

    @Test
    fun `get 409 CONFLICT if client tries to decide ID`() {

        val dto = getPlantDTO()
        dto.id = 42
        post(dto)
                .statusCode(409)
    }

    @Test
    fun `get 400 BAD REQUEST on breaking constraint`() {

        val dto = getPlantDTO()
        dto.name = "a"
        post(dto)
                .statusCode(400)
    }

    @Test
    fun `can update a plant`() {

        val dto = postAndGet()

        val originalName = dto.name
        val newName = "Updated name"
        dto.name = newName

        put(dto)
                .then()
                .statusCode(204)

        val retrieved = given()
                .get("plants/${dto.id}")
                .then()
                .statusCode(200)
                .extract()
                .`as`(PlantDto::class.java)

        assertNotEquals(retrieved.name, originalName)
        assertEquals(retrieved.name, newName)
    }

    @Test
    fun `returns bad request if any part of DTO is missing`() {

        val dto = postAndGet()

        dto.name = null;
        put(dto)
                .then()
                .statusCode(400)
    }

    @Test
    fun `PUT on non-existing resource retuns 404`() {

        val dto = getPlantDTO()
        dto.id = 999

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", dto.id)
                .get("plants/{id}")
                .then()
                .statusCode(404) //verifying that it actually does not exist

        put(dto)
                .then()
                .statusCode(404)
    }


    private fun getAll() = given()
        .contentType(ContentType.JSON)
        .get("/plants")
        .then()

    private fun put(updated: PlantDto) = given()
        .contentType(ContentType.JSON)
        .body(updated)
        .pathParam("id", updated.id)
        .put("plants/{id}")


    private fun postAndGet(): PlantDto {

        val location = post(getPlantDTO())
                .statusCode(201)
                .extract()
                .header("Location")

        return given()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .`as`(PlantDto::class.java)
    }

    private fun post(dto: PlantDto) = given()
            .contentType(ContentType.JSON)
            .body(dto)
            .post("/plants")
            .then()
}