package org.olaven.enterprise.trees.controller

import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

class CacheTest : ControllerTestBase() {

    @Autowired
    private lateinit var locationController: LocationController
    @Autowired
    private lateinit var plantController: PlantController

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
    fun `getting locations is cahced`() {

        val before = locationController.callCount.getAll
        repeat((0..10).count()) {

            given()
                    .get("/locations")
        }
        val after = locationController.callCount.getAll

        assertEquals(0, before)
        assertEquals(1, after)
    }
}