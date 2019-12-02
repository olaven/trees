package org.olaven.trees.api.annotations

import org.olaven.trees.api.dto.LocationDTO
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class IsLocationValidator: ConstraintValidator<IsLocation, LocationDTO> {

    override fun initialize(constraintAnnotation: IsLocation?) {

    }

    override fun isValid(location: LocationDTO?, context: ConstraintValidatorContext?): Boolean {

        var validLatitude = false;
        var validLongitude = false

        // -180 / +180
        location?.let {

            location.x?.let {

                validLatitude =
                        location.x!! >= -90 &&
                                location.x!! <= 90
            }

            location.y?.let {

                validLongitude =
                        location.y!! >= -180 &&
                                location.y!! <= 180
            }
        }

        return validLatitude && validLongitude
    }
}