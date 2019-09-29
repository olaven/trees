package org.olaven.enterprise.trees

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import com.netflix.config.ConfigurationManager


fun main(args: Array<String>) {
    SpringApplication.run(TreeApplication::class.java, "--spring.profiles.active=local")
}

@SpringBootApplication
@EnableSwagger2
open class TreeApplication {

    /*
        Bean used to configure Swagger documentation
     */
    @Bean
    open fun swaggerApi(): Docket {
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

    init {
        //Hystrix configuration
        ConfigurationManager.getConfigInstance().apply {
            // how long to wait before giving up a request?
            setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 500) //default is 1000
            // how many failures before activating the CB?
            setProperty("hystrix.command.default.circuitBreaker.requestVolumeThreshold", 2) //default 20
            setProperty("hystrix.command.default.circuitBreaker.errorThresholdPercentage", 50)
            //for how long should the CB stop requests? after this, 1 single request will try to check if remote server is ok
            setProperty("hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds", 5000)
        }
    }
}
