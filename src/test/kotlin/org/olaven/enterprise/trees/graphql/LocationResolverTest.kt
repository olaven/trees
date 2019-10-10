package org.olaven.enterprise.trees.graphql

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.olaven.enterprise.trees.WebTestBase

class LocationResolverTest: WebTestBase(excludeBasePath = true) {

    @Test
    fun `getting locations returns same size as in db`() {

        val n = 10;
        assertThat(locationRepository.findAll().count())
                .isEqualTo(0)

        repeat((0 until n).count()) {

            persistLocation(getLocationDTO())
        }

        assertThat((locationRepository.findAll().count()))
                .isEqualTo(n)

        given().accept(ContentType.JSON)
                .queryParam("query", "{locations{id}}")
                .get("/graphql")
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.locations.size()", equalTo(n))
    }

    @Test
    fun `can get specific location`() {

        val entity = persistLocation(getLocationDTO())
        given().accept(ContentType.JSON)
                .queryParam("query", "{location(id: ${entity.id}){id, x, y}}")
                .get("/graphql")
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.location.id", equalTo(entity.id?.toInt()))
    }

    @Test
    fun `can create location`() {

        val id = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" : "mutation{createLocation(location: {x: 34.4, y: 44.3})}" }
                    """.trimIndent())
                .post("/graphql")
                .then()
                .statusCode(200)
                .extract().jsonPath()
                .getLong("data.createLocation")

        val retrievedLocation = locationRepository.findById(id)
        assertThat(retrievedLocation)
                .isPresent
    }

    @Test
    fun `can update location`() {

        val original = 10.0
        val updated = 20.0

        // First create
        val locationID = persistLocation(getLocationDTO()).id
        val id = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" : "mutation{createLocation(location: {x: $original, y: 44.3})}" }
                    """.trimIndent())
                .post("/graphql")
                .then()
                .statusCode(200)
                .extract().jsonPath()
                .getLong("data.createLocation")


        assertThat(locationRepository.findById(id).get().x)
                .isEqualTo(original)

        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" : "mutation{updateLocation(id: $id, location: {x: $updated, y: 44.3})}" }
                    """.trimIndent())
                .post("/graphql")
                .then()
                .statusCode(200)

        assertThat(locationRepository.findById(id).get().x)
                .isNotEqualTo(original)
                .isEqualTo(updated)
    }
}