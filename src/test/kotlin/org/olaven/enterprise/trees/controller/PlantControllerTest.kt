package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.olaven.enterprise.trees.TreeApplication
import org.olaven.enterprise.trees.dto.PlantDto
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ TreeApplication::class ], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class PlantControllerTest: ControllerTestBase() {

    @Test
    fun `can create a plant`() {

        val name = "My plant";
        val description = "Green and refreshin";
        val height = 20.0;
        val age = 40;

        val dto = PlantDto(name, description, height, age)
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
    fun `can create and retrieve a plant`() {

        val name = "My plant";
        val description = "Green and refreshin";

        val dto = PlantDto(name, description, 29.0, 2)
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
    fun `get 409 CONFLICT if client tries to decide ID`() {

        val dto = getDTO()
        dto.id = 42
        post(dto)
                .statusCode(409)
    }

    @Test
    fun `get 400 BAD REQUEST on breaking constraint`() {

        val dto = getDTO()
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

        val dto = getDTO()
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

    private fun put(updated: PlantDto) = given()
                .contentType(ContentType.JSON)
                .body(updated)
                .pathParam("id", updated.id)
                .put("plants/{id}")


    private fun postAndGet(): PlantDto {

        val location = post(getDTO())
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

    private fun getDTO(): PlantDto {

        val name = faker.funnyName().name()
        val description = faker.lorem().paragraph()
        val height = faker.number().randomDouble(2, 1, 100)
        val age = faker.number().numberBetween(4, 20)

        return PlantDto(name, description, height, age)
    }
}