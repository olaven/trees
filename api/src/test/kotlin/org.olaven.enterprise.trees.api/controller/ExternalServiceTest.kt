package org.olaven.enterprise.trees.api.controller

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.olaven.enterprise.trees.api.WebTestBase

internal class ExternalServiceTest: WebTestBase() {


    companion object {

        private var wiremockServer: WireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().port(8099).notifier(ConsoleNotifier(true)))

        @BeforeAll
        @JvmStatic
        fun `init wiremock`() {

            wiremockServer.start()
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            wiremockServer.stop()
        }

    }

    @Test
    fun `getting external gives 200`() {

        stubresponse()
        given()
                .accept(ContentType.JSON)
                .get("/external")
                .then()
                .statusCode(200)
    }


    private fun stubresponse() {

        val json = """
        [
            { "userId": 1, "id": 1, "title": "Title 1", "completed": false },
            { "userId": 1, "id": 2, "title": "Title 2", "completed": true },
            { "userId": 2, "id": 3, "title": "Title 3", "completed": true },
            { "userId": 3, "id": 4, "title": "Title 4", "completed": false },
            { "userId": 2, "id": 5, "title": "Title 5", "completed": true }
        ]
        """

        wiremockServer.stubFor(//prepare a stubbed response for the given request
                WireMock.get(//define the GET request to mock
                        /*
                           recall regular expressions:
                           "." =  any character
                           "*" = zero or more times
                        */
                        WireMock.urlMatching("/api/external"))
                        // define the mocked response of the GET
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + json.toByteArray(charset("utf-8")).size)
                                .withBody(json)))
    }
}