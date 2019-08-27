package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.olaven.enterprise.trees.TreeApplication
import org.olaven.enterprise.trees.dto.PlantDto
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertNotNull

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ TreeApplication::class ], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class PlantControllerTest: ControllerTestBase() {

    @Test
    fun someTest() {

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
}