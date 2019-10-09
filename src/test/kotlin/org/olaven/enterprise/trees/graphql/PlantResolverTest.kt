package org.olaven.enterprise.trees.graphql

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.olaven.enterprise.trees.WebTestBase

class PlantResolverTest: WebTestBase(excludeBasePath = true) {

    @Test
    fun `getting plants returns same size as in db`() {

        val n = 10;
        Assertions.assertThat(plantRepository.findAll().count())
                .isEqualTo(0)

        repeat((0 until n).count()) {

            persistPlant(getPlantDTO())
        }

        Assertions.assertThat((plantRepository.findAll().count()))
                .isEqualTo(n)

        RestAssured.given().accept(ContentType.JSON)
                .queryParam("query", "{plants{id}}")
                .get("/graphql")
                .then()
                .statusCode(200)
                .body("$", Matchers.hasKey("data"))
                .body("$", Matchers.not(Matchers.hasKey("errors")))
                .body("data.plants.size()", Matchers.equalTo(n))
    }

    @Test
    fun `can get specific plant`() {

        val entity = persistPlant(getPlantDTO())
        RestAssured.given().accept(ContentType.JSON)
                .queryParam("query", "{plant(id: ${entity.id}){id}}")
                .get("/graphql")
                .then()
                .statusCode(200)
                .body("$", Matchers.hasKey("data"))
                .body("$", Matchers.not(Matchers.hasKey("errors")))
                .body("data.plant.id", Matchers.equalTo(entity.id.toInt()))
    }

    @Test
    fun `can get location object through plant with graphQL`() {

        val location = persistLocation(getLocationDTO())
        val plant = persistPlant(getPlantDTO(location))

        RestAssured.given().accept(ContentType.JSON)
                .queryParam("query", """
                    {plant(id: ${plant.id}){
                        location {
                            id
                        }
                    }}
                """.trimIndent())
                .get("/graphql")
                .then()
                .statusCode(200)
                .body("$", Matchers.hasKey("data"))
                .body("$", Matchers.not(Matchers.hasKey("errors")))
                .body("data.plant.location.id", Matchers.equalTo(location.id?.toInt()))
    }
}