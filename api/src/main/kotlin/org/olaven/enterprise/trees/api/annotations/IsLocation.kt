package org.olaven.enterprise.trees.api.annotations

import java.lang.annotation.Documented
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass


@Constraint(validatedBy = [IsLocationValidator::class])
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Documented
annotation class IsLocation (

        val message: String = "Location is invalid",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

