package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.olaven.enterprise.trees.TreeApplication
import org.olaven.enterprise.trees.dto.PlantDto
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals
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
        dto.name = "a" //NOTE: only one character //TODO: This fails, as constraintvioloation is not occurring, it seems like p '´
        post(dto)
                .statusCode(400)
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