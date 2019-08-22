package org.olaven.enterprise.trees.controller

import org.olaven.enterprise.trees.TreeApplication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import io.restassured.RestAssured.*
import io.restassured.http.ContentType
import org.olaven.enterprise.trees.dto.TreeDto
import kotlin.test.assertNotNull

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TreeControllerTest {

    @Test
    fun someTest() {

        val name = "My Tree";
        val description = "Tall and nice";
        val height = 20.0;
        val age = 40;

        val dto = TreeDto(name, description, height, age)
        val id = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post("trees/")
                .then()
                .statusCode(201)
                .extract().asString();

        assertNotNull(id);
    }
}