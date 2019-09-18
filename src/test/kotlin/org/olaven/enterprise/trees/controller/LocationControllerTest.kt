package org.olaven.enterprise.trees.controller

import com.fasterxml.jackson.databind.JsonNode
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class LocationControllerTest: ControllerTestBase() {


    @Test
    fun `database has none of this entity before tests run`() {

        getAll()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("data.list.size()", CoreMatchers.`is`(0))
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
    fun `can get locations`() {

        val n = 5;
        persistLocations(n)

        getAll()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("data.list.size()", CoreMatchers.`is`(n))
    }


    @Test
    fun `get all-endpoint returns wrapped response`() {

        val location = getLocationDTO()
        persistLocation(location)

        getAll()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("data.list.size()", equalTo(1))
    }

    @Test
    fun `a page only contains 5 items`() {

        val total = 6
        val expected = 5

        persistLocations(total)
        getAll()
                .contentType(ContentType.JSON)
                .body("data.list.size()", equalTo(expected))
    }

    @Test
    fun `can navigate using "next" on page`() {

        val all = 15
        persistLocations(all)

        /*
        * There's a lot of repetition here.
        * However, the repetition proves the point
        * of the test
        * */

        val allLocations = locationRepository.findAll()
        println(allLocations)
        val secondPage = getAll()
                .body("data.list.size()", equalTo(5))
                .extract()
                .jsonPath()
                .get<String>("data.next")

        val thirdPage = getAll(secondPage)
                .body("data.list.size()", equalTo(5))
                .extract()
                .jsonPath()
                .get<String>("data.next")

        val fourthPage = getAll(thirdPage)
                .body("data.list.size()", equalTo(5))
                .extract()
                .jsonPath()
                .get<String>("data.next")

        val fifthPage = getAll(fourthPage)
                .body("data.list.size()", equalTo(0))
                .extract()
                .jsonPath()
                .get<String>("data.next")

        assertNull(fifthPage)
    }


    @Test
    fun `locations does not include plants by default`() {

        persistLocations(1, 1)
        given()
                .contentType(ContentType.JSON)
                .get("/locations")
                .then()
                .extract()
                .jsonPath()
                .getList<HashMap<String, JsonNode>>("data.list")
                .forEach {

                    val plants = it["plants"]!!.asIterable()
                    print(plants);
                    assertEquals(0, plants.count())
                }

    }

    @Test
    fun `locations includes plants with correct expand parameter`() {

        assertEquals(0, locationRepository.findAll().count())
        assertEquals(0, plantRepository.findAll().count())

        persistLocations(1, 1)
        assertEquals(1, locationRepository.findAll().count())
        assertEquals(1, plantRepository.findAll().count())
        given()
                .contentType(ContentType.JSON)
                .get("/locations?expand=PLANTS")
                .then()
                .extract()
                .jsonPath()
                .getList<HashMap<String, JsonNode>>("data.list")
                .forEach {

                    val plants = it["plants"]!!.asIterable()
                    print(plants);
                    assertEquals(1, plants.count())
                }

    }

    private fun persistLocations(count: Int, plantsPerLocation: Int = 0) {

        (0 until count).forEach { _ ->

            val location = getLocationDTO()
            val locationEntity = persistLocation(location)

            (0 until  plantsPerLocation).forEach { _ ->

                val plant = getPlantDTO(locationEntity)
                persistPlant(plant)
            }
        }
    }

    private fun getAll(location: String? = null): ValidatableResponse {

        val path =
                location ?: "/locations"

        return given()
                .contentType(ContentType.JSON)
                .get(path)
                .then()
    }
}