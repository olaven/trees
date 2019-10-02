package org.olaven.enterprise.trees.controller

import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

/**
 * This controller is made just for the sake
 * of calling an external service, to showcase
 * mocking in tests.
 *
 * To be clear: this is just an example.
 */
@RestController
@Api(value ="api/external", description = "calling external service")
@RequestMapping(value = ["api/external"])
class ExternalService {

    @GetMapping("")
    fun callExternalService(): ResponseEntity<String> {

        //https://jsonplaceholder.typicode.com/todos
        val uri = UriComponentsBuilder
                .fromUriString("https://jsonplaceholder.typicode.com/todos")
                .build()
                .toUri()
        val client = RestTemplate()
        val response = client.getForEntity(uri, String::class.java)

        if (response.statusCode.is2xxSuccessful)
            return ResponseEntity.ok(response.body)

        return ResponseEntity.status(500).build<String>()

    }
}