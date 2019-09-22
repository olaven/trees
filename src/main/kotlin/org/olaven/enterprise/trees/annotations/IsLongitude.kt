package org.olaven.enterprise.trees.annotations

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class IsLongitudeValidator: ConstraintValidator<IsLongitude, Double> {

    override fun isValid(value: Double?, p1: ConstraintValidatorContext?): Boolean {

        // -180 / +180
        value?.let {

            return value <= 180 && value >= -180
        }

        return false;
    }
}

@Constraint(validatedBy = [IsLongitudeValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME) //TODO: check if works
@MustBeDocumented
annotation class IsLongitude (

        val message: String = "Longitude is invalid"
)

