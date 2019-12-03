package org.olaven.trees.api.org.olaven.trees.api

import org.olaven.trees.api.TreeApplication
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter


//NOTE: runs on 8080
fun main() {
    SpringApplication.run(TreeApplication::class.java, "--spring.profiles.active=local")
}

@Profile("local")
@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurerAdapter() {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
    }
}