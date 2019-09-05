package org.olaven.enterprise.trees.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(value ="trees/api/", description = "Welcome-message and instructions for the API.")
@RequestMapping(value = ["trees/api/"])
class WelcomeController {

                                        //is default because first
    @GetMapping(value = ["/"], produces = ["text/plain;charset=UTF-8", "text/plain;charset=ISO-8859-1"])
    @ApiOperation("Get the instructions")
    fun getWelcomeMessage(      //headers are _not_ case-sensitive
            @RequestHeader(value = "Accept-Language", required = false) language: String?
    ): String {

        return if (language != null && language.trim() == "no") {

            "Velkommen! Du finner dokumentasjon her: /api/swagger-ui.html. Ha det gøy."
        } else {

            "Welcome! You wil find documentation here: /api/swagger-ui.html. Have fun."
        }
    }
}