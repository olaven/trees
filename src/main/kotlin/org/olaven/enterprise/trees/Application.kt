package org.olaven.enterprise.trees

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class TreeApplication
fun main(args: Array<String>) {
    SpringApplication.run(TreeApplication::class.java, *args)
}
