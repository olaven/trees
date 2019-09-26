package org.olaven.enterprise.trees.controller

import com.fasterxml.jackson.databind.JsonNode
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.olaven.enterprise.trees.dto.PlantDto
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class LocationControllerTest(): ControllerTestBase() {


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


    @Test
    fun `longitude greater than 180 gives 400`() {

        val dto = getLocationDTO()
        dto.y = 181.0
        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post("/locations")
                .then()
                .statusCode(400)
    }

    fun `getting random returns a location`() {

        persistLocations(5)
        getRandom()
                .body("data", notNullValue())
    }

    @Test
    fun `redirection on random is temporary`() {

        persistLocations(5)
        getRandom(false)
                .statusCode(307)
    }

    @Test
    fun `returns 404 when no locations are available`() {

        persistLocations(0) //could skip this line, just to make it explicit
        getRandom()
                .statusCode(404)
    }


    //NOTE: may fail due to the randomness being tested
    @Test
    fun `random locations have a reasonable distribution`() {

        val total = 20
        val distributionMap = HashMap<Int, Int?>()
        persistLocations(total)
        repeat((0..100).count()) {

            val id = getRandom()
                    .extract()
                    .jsonPath()
                    .get<Int>("data.id")

            if (distributionMap[id] == null) {

                distributionMap[id] = 0
            }


            val currentCount = distributionMap[id as Int]!!
            val updatedCount = currentCount + 1
            distributionMap[id] = updatedCount
        }

        distributionMap.values.forEach {

            //could fail because of randomness, but should be ~5 -  100/20
            assertNotNull(it)
            assertTrue(it > 0)
            assertTrue(it < 15)
        }
    }

    @Test
    fun `random locations returns no plants by default`() {

        persistLocations(1, 1)
        getRandom(includePlants = false)
                .body("data.plants", hasSize<PlantDto>(0))
    }

    @Test
    fun `random locations returns plants if ?expand=PLANTS`() {

        persistLocations(1, 1)
        getRandom(includePlants = true)
                .body("data.plants", hasSize<PlantDto>(1))
    }

    @Test
    fun `locations return with an e-tag`() {

        val entity = persistLocation(getLocationDTO())
        getSpecific(entity.id!!)
                .statusCode(200)
                .header("ETag", notNullValue())
    }


    @Test
    fun `returns 304 if unmodified ETag supplied`() {

        val entity = persistLocation(getLocationDTO())
        val etag = getSpecific(entity.id!!)
                .statusCode(200)
                .extract().header("ETag")

        given()
                .header("if-none-match", etag)
                .get("/locations/${entity.id}")
                .then()
                .statusCode(304)
    }

    @Test
    fun `location returns with last-modified header`() {

        val entity = persistLocation(getLocationDTO())
        getSpecific(entity.id!!)
                .header("last-modified", notNullValue())
    }

    @Test
    fun `returns 304 if modified sine`() {

        val entity = persistLocation(getLocationDTO())
        val lastModified =getSpecific(entity.id!!)
                .statusCode(200)
                .extract().header("last-modified")

        given()
                .header("If-Modified-Since", lastModified)
                .get("/locations/${entity.id}")
                .then()
                .statusCode(304)

    }

    private fun getRandom(followRedirect: Boolean = true, includePlants: Boolean = false): ValidatableResponse {

        val path = if (includePlants)
            "/locations/random?expand=PLANTS"
        else
            "/locations/random"

        return given()
                .redirects().follow(followRedirect)
                .get(path)
                .then()
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

    private fun getSpecific(id: Long) = given()
            .get("/locations/$id")
            .then()

}