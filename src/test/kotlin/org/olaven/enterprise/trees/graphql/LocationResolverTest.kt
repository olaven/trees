package org.olaven.enterprise.trees.graphql

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Disabled
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

    @Test @Disabled
    fun `can create location`() {


    }

    @Test @Disabled
    fun `can update location`() {

    }
}