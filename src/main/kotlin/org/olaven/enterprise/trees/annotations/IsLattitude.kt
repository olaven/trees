package org.olaven.enterprise.trees.annotations

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class IsLattitudeValidator: ConstraintValidator<IsLongitude, Double> {

    override fun isValid(value: Double?, p1: ConstraintValidatorContext?): Boolean {

        // -90 / +90
        value?.let {

            return value <= 90 && value >= -90
        }

        return false;
    }
}

@Constraint(validatedBy = [IsLongitudeValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME) //TODO: check if works
@MustBeDocumented
annotation class IsLattitude (

        val message: String = "X and Y has to be valid coordinate"
)

