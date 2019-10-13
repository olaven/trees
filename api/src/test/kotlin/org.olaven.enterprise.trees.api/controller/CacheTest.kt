package org.olaven.enterprise.trees.api.controller

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.olaven.enterprise.trees.api.WebTestBase
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

class CacheTest : WebTestBase() {

    @Autowired
    private lateinit var locationController: LocationController
    @Autowired
    private lateinit var plantController: PlantController

    @BeforeEach
    fun `init cache tests`() {

        locationController.callCount.getAll = 0
        locationController.callCount.getOne = 0

        plantController.callCount.getAll = 0 //NOTE not used yet
        plantController.callCount.getOne = 0
    }

    @Test
    fun `callcount is registered on location`() {

        val before = locationController.callCount.getAll
        given()
                .get("/locations")
        val after = locationController.callCount.getAll

        assertEquals(0, before)
        assertEquals(1, after)
    }

    @Test
    fun `all locations has max-age`() {

        given()
                .get("/locations")
                .then()
                .statusCode(200)
                .header("cache-control", containsString("max-age"))
    }

    @Test
    fun `specific location has max-age`() {

        val entity = persistLocation(getLocationDTO())
        given()
                .get("/locations/${entity.id}")
                .then()
                .statusCode(200)
                .header("cache-control", containsString("max-age"))
    }

    @Test
    fun `locations are cached`() {

        val before = locationController.callCount.getAll
        repeat((0..10).count()) {

            given()
                    .param("port", port)
                    .get("/cacheproxy/locations")
                    .then()
                    .statusCode(200)


        }
        val after = locationController.callCount.getAll

        assertEquals(0, before)
        assertEquals(1, after)
    }

    @Test
    fun `getting specific location is cached`() {

        val before = locationController.callCount.getOne
        val location = persistLocation(getLocationDTO())
        repeat((0..10).count()) {

            given()
                    .param("port", port)
                    .get("/cacheproxy/locations/${location.id}")

        }
        val after = locationController.callCount.getOne

        assertEquals(0, before)
        assertEquals(1, after)
    }

    @Test
    fun `getting specific plant is cached`() {

        val before = plantController.callCount.getOne

        val plant = persistPlant(getPlantDTO())
        repeat((0..10).count()) {

            given()
                    .param("port", port)
                    .get("/cacheproxy/plants/${plant.id}")
                    .then()
                    .statusCode(200)
        }

        val after = plantController.callCount.getOne

        assertEquals(0, before)
        assertEquals(1, after)
    }
}