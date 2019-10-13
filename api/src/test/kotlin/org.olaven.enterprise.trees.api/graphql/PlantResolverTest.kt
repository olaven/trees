package org.olaven.enterprise.trees.api.graphql

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasKey
import org.hamcrest.Matchers.isEmptyOrNullString
import org.junit.jupiter.api.Test
import org.olaven.enterprise.trees.api.WebTestBase

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
                .body("$", hasKey("data"))
                .body("$", Matchers.not(hasKey("errors")))
                .body("data.plants.size()", Matchers.equalTo(n))
    }

    @Test
    fun `can get specific plant`() {

        val entity = persistPlant(getPlantDTO())
        given().accept(ContentType.JSON)
                .queryParam("query", "{plant(id: ${entity.id}){id}}")
                .get("/graphql")
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", Matchers.not(hasKey("errors")))
                .body("data.plant.id", Matchers.equalTo(entity.id.toInt()))
    }

    @Test
    fun `can get location object through plant with graphQL`() {

        val location = persistLocation(getLocationDTO())
        val plant = persistPlant(getPlantDTO(location))

        given().accept(ContentType.JSON)
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
                .body("$", hasKey("data"))
                .body("$", Matchers.not(hasKey("errors")))
                .body("data.plant.location.id", Matchers.equalTo(location.id?.toInt()))
    }

    @Test
    fun `can create plant`() {

        val locationID = persistLocation(getLocationDTO()).id
        val id = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" : "mutation{createPlant(plant: {name:\"my name\", description:\"some desc\", height: 34.4, age: 18, location: $locationID})}" }
                    """.trimIndent())
                .post("/graphql")
                .then()
                .statusCode(200)
                .extract().jsonPath()
                .getLong("data.createPlant")

        val retrievedPlant = plantRepository.findById(id)
        assertThat(retrievedPlant)
                .isPresent
    }

    @Test
    fun `returns appropriate error if location does not exist`() {

        val locationID = -1L
        val location = locationRepository.findById(locationID)
        assertThat(location)
                .isNotPresent

        val body = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" : "mutation{createPlant(plant: {name:\"my name\", description:\"some desc\", height: 34.4, age: 18, location: $locationID})}" }
                    """.trimIndent())
                .post("/graphql")
                .then()
                .statusCode(200)
                .body("data.createPlant", isEmptyOrNullString())
                .extract().body().asString()

        assertThat(body)
                .contains("Given location does not exist.")
    }

    @Test
    fun `returns appropriate error on constraint violation`() {

        val location = persistLocation(getLocationDTO())
        val body = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON) //NOTE: name is to short
                .body(""" 
                    { "query" : "mutation{createPlant(plant: {name:\"x\", description:\"some desc\", height: 34.4, age: 18, location: ${location.id}})}" }
                    """.trimIndent())
                .post("/graphql")
                .then()
                .statusCode(200)
                .body("data.createPlant", isEmptyOrNullString()) //TODO update actual test
                .extract().body().asString()

        assertThat(body)
                .contains("Validation failed for classes")
    }

    @Test
    fun `can update plant`() {

        val original = "original name"
        val updated = "updated name"
        
        // First create 
        val locationID = persistLocation(getLocationDTO()).id
        val id = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" : "mutation{createPlant(plant: {name:\"$original\", description:\"some desc\", height: 34.4, age: 18, location: $locationID})}" }
                    """.trimIndent())
                .post("/graphql")
                .then()
                .statusCode(200)
                .extract().jsonPath()
                .getLong("data.createPlant")
        
        
        assertThat(plantRepository.findById(id).get().name)
                .isEqualTo(original)

        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" : "mutation{updatePlant(id: $id, plant: {name:\"$updated\", description:\"some desc\", height: 34.4, age: 18, location: $locationID})}" }
                    """.trimIndent())
                .post("/graphql")
                .then()
                .statusCode(200)

        assertThat(plantRepository.findById(id).get().name)
                .isNotEqualTo(original)
                .isEqualTo(updated)
    }
}