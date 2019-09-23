package org.olaven.enterprise.trees.annotations

import org.olaven.enterprise.trees.dto.LocationDTO
import java.lang.annotation.Documented
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.constraints.NotNull


@NotNull
@Constraint(validatedBy = [IsLocationValidator::class])
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Documented
annotation class IsLocation (

        val message: String = "Location is invalid"
)

