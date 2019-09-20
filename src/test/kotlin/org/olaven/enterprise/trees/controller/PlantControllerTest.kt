package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.olaven.enterprise.trees.dto.LocationDTO
import org.olaven.enterprise.trees.dto.PlantDto
import org.olaven.enterprise.trees.dto.Status
import kotlin.test.assertNotEquals

internal class PlantControllerTest: ControllerTestBase() {

    @Test 
    fun `database has none of this entity before tests run`() {

        getAll()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("list.size()", CoreMatchers.`is`(0))
    }

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
    fun `can create and retrieve a plant`() {

        val dto = getPlantDTO()
        val location = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post("plants/")
                .then()
                .statusCode(201)
                .extract().header("Location")


        given()
                .get(location)
                .then()
                .statusCode(200)
                .body("data.name", Matchers.equalTo(dto.name))
                .body("data.age", Matchers.equalTo(dto.age))
                .body("data.description", Matchers.equalTo(dto.description))
    }


    @Test
    fun `can retrieve all plants`() {

        val n = 5;
        repeat((0 until n).count()) {
            post(getPlantDTO())
        }

        given()
                .contentType(ContentType.JSON)
                .get("/plants")
                .then()
                .body("list.size()", CoreMatchers.`is`(n))
    }

    @Test
    fun `getting all returns wrapped responses`() {

        val posted = postAndGet()
        given()
                .contentType(ContentType.JSON)
                .get("/plants")
                .then()
                .statusCode(200)
                .body("code", Matchers.hasItem(200))
                .body("data.name", Matchers.hasItem(posted?.name))
                .body("status", Matchers.hasItem(Status.SUCCESS.toString()))
    }

    @Test
    fun `can retrieve all locations`() {

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

        val originalName = dto?.name
        val newName = "Updated name"
        dto?.name = newName

        put(dto!!)
                .then()
                .statusCode(204)

        given()
                .get("plants/${dto.id}")
                .then()
                .statusCode(200)
                .body("data.name", Matchers.not(Matchers.equalTo(originalName)))
                .body("data.name", Matchers.equalTo(newName))
    }

    @Test
    fun `returns bad request if any part of DTO is missing`() {

        val dto = postAndGet()!!

        dto.name = null
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

    @Test
    fun `POST with constraint validation throws error`() {

        val dto = getPlantDTO()
        dto.name = null
        post(dto)
                .statusCode(400)
                .body("message", containsString("was invalid"))
    }

    @Test
    fun `a plant requires location`() {

        // Kotlin typing prevents me from giving bad location
        val dto = """
            {
                "name": "Darryl Likt",
                "description": "Voluptates porro earum.",
                "height": 28.17,
                "age": 13,
                "location": null,
                "id": null
            }
        """.trimIndent()

        postRaw(dto)
                .statusCode(400)
    }


    @Test
    private fun `can update plant`() {

        val original = postAndGet()!!
        val newName = "SOME UPDATED NAME"

        val json = """
            {
                name: ${newName}, 
                description: ${original.description},
                height: ${original.description},  
                age: ${original.age}, 
                location: {
                    x: ${original.location?.x},
                    y: ${original.location?.y},
                    id: ${original.location?.id}
                }
            }
        """.trimIndent()

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .put("plants/${original.id}")
                .then()
                .statusCode(204)

        val updated = get(original.id!!)
                .extract()
                .`as`(PlantDto::class.java)

        assertNotEquals(original.name, newName)
        assertEquals(newName, updated.name)
    }



    @Test
    fun `return appropriate code on bad PATCH request`() {

        val original = postAndGet()!!
        val wrongValue = "NOT AN INTEGER"

        val json = """
            {
                name: ${original.name}, 
                description: ${original.description},
                height: ${original.description},  
                age: ${wrongValue}, 
                location: {
                    x: ${original.location?.x},
                    y: ${original.location?.y},
                    id: ${original.location?.id}
                }
            }
        """.trimIndent()

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .put("plants/${original.id}")
                .then()
                .statusCode(400)
    }



    private fun getAll() = given()
        .contentType(ContentType.JSON)
        .get("/plants")
        .then()

    private fun get(id: Long) = given()
            .contentType(ContentType.JSON)
            .get("/plants/$id")
            .then()

    private fun put(updated: PlantDto) = given()
        .contentType(ContentType.JSON)
        .body(updated)
        .pathParam("id", updated.id)
        .put("plants/{id}")


    private fun postAndGet(): PlantDto? {

        val location = post(getPlantDTO())
                .statusCode(201)
                .extract()
                .header("Location")

        println(location)
        val json = given()
                .get(location)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()

        return if (json.get<PlantDto?>("data") != null) {

            PlantDto(
                    name = json.get("data.name"),
                    description = json.get("data.description"),
                    height = (json.get("data.height") as Float).toDouble(),
                    age = json.get("data.age"),
                    location = LocationDTO(
                            (json.get("data.location.x") as Float).toDouble(),
                            (json.get("data.location.y") as Float).toDouble(),
                            json.get<Int>("data.location.id").toString().toLong()
                    ),
                    id = json.get<Int>("data.id").toString().toLong()
            )
        } else {

            null;
        }
    }

    private fun post(dto: PlantDto) = given()
            .contentType(ContentType.JSON)
            .body(dto)
            .post("/plants")
            .then()

    private fun postRaw(body: String) = given()
            .contentType(ContentType.JSON)
            .body(body)
            .post("/plants")
            .then()
}