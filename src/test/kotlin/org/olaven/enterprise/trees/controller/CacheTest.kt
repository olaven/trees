package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

class CacheTest : ControllerTestBase() {

    @Autowired
    private lateinit var locationController: LocationController
    @Autowired
    private lateinit var plantController: PlantController

    @BeforeEach
    fun `init cache tests`() {

        locationController.callCount.getAll = 0
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
    fun `response has max-age`() {

        given()
                .get("/locations")
                .then()
                .header("cache-control", containsString("max-age"))
    }

    @Test
    fun `getting locations is cached`() {

        val before = locationController.callCount.getAll
        repeat((0..10).count()) {

            given()
                    .param("port", port)
                    .get("/cacheproxy/locations")

        }
        val after = locationController.callCount.getAll

        assertEquals(0, before)
        assertEquals(1, after)
    }
}