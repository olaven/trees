package org.olaven.enterprise.trees.entity

import org.jetbrains.annotations.NotNull
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Size

@Entity
class PlantEntity (


    /*
    * NOTE: '@field' is specifying that the
    * annotation should be applied to
    * the class field, not the constructor
    * (`val name: String`is part of a Kotlin constructor to begin with)
    * */
    @field:Size(min = 2, max = 100)
    var name: String,

    @field:Size(min = 2, max = 500)
    var description: String,

    @field:Min(0)
    var height: Double,

    @field:NotNull
    @field:Min(0)
    @field:Max(15_000)
    var age: Int,

    @field:NotNull
    @field:ManyToOne
    var location: LocationEntity,

    @field:Id
    @field:GeneratedValue
    var id: Long = -1
)