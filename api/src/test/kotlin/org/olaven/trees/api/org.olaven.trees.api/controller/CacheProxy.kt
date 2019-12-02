package org.olaven.trees.api.controller

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
    * caching is actually occurring.
    * */


    @GetMapping("/locations")
    fun getLocationsProxy(
            @RequestParam(value = "port", required = true)
            port: Int
    ) = callOrigin("locations", port)


    @GetMapping("/locations/{id}")
    fun getLocationProxy(
            @PathVariable(required = true)
            id: Long,
            @RequestParam(value = "port", required = true)
            port: Int
    ) = callOrigin("locations/$id", port)

    @GetMapping("/plants/{id}")
    fun getTreeProxy(
            @PathVariable(required = true)
            id: Long,
            @RequestParam(value = "port", required = true)
            port: Int
    ) = callOrigin("plants/$id", port)

    private fun callOrigin(path: String, port: Int): ResponseEntity<String> {

        val url = "http://localhost:$port/api/$path"
        val result =  restTemplate.getForEntity(url, String::class.java)
        return result;
    }
}