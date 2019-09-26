package org.olaven.enterprise.trees.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping(value = ["api/cacheproxy"])
class CacheProxy(
        private val restTemplate: RestTemplate
) {

    /*
    * Calling the cached service through
    * this proxy.
    *
    * Otherwise, Spring does not cache.
    *
    * This way, I am able to test that the
    * caching is actually occuring.
    * */


    @GetMapping("/locations")
    fun getLocationsProxy(
            @RequestParam(value = "port", required = true)
            port: Int
    ): ResponseEntity<String> {

        val res =
                restTemplate.getForEntity("http://localhost:$port/api/locations", String::class.java)

        return ResponseEntity.ok(res.body)
    }


    @GetMapping("/locations/{id}")
    fun getLocationProxy(
            @PathVariable(required = true)
            id: Long,
            @RequestParam(value = "port", required = true)
            port: Int
    ): ResponseEntity<String> {

        val res =
                restTemplate.getForEntity("http://localhost:$port/api/locations/$id", String::class.java)

        return ResponseEntity.ok(res.body)
    }

}