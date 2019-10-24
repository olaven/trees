package org.olaven.enterprise.trees.api

import com.netflix.config.ConfigurationManager
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


fun main(args: Array<String>) {
    SpringApplication.run(TreeApplication::class.java, "--spring.profiles.active=local")
}

@SpringBootApplication
@EnableCaching
@EnableSwagger2
@EnableEurekaClient
class TreeApplication { //TODO: rename to API-application

    /*
        Bean used to configure Swagger documentation
     */
    @Bean
    fun swaggerApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("API for Trees.")
                .description("Description coming")
                .version("0.0.1")
                .build()
    }


    /*
    * NOTE: Hystrix config is a copied and slightly modified version of 
    * https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/d013eaf656f053c3ca1fc443fd507939a8e7b684/advanced/rest/circuit-breaker/src/main/kotlin/org/tsdes/advanced/rest/circuitbreaker/CircuitBreakerApplication.kt
    * */
    init {
        //Hystrix configuration
        ConfigurationManager.getConfigInstance().apply {
            // how long to wait before giving up a request?
            setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 500) //default is 1000
            // how many failures before activating the CB?
            setProperty("hystrix.command.default.circuitBreaker.requestVolumeThreshold", 10) //default 20
            setProperty("hystrix.command.default.circuitBreaker.errorThresholdPercentage", 40)
            //for how long should the CB stop requests? after this, 1 single request will try to check if remote server is ok
            setProperty("hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds", 5000)
        }
    }
}
