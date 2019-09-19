package org.olaven.enterprise.trees.entity

import org.jetbrains.annotations.NotNull
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
open class LocationEntity (

        @field:NotNull
        @field:Min(0)
        val x: Double?,

        @field:NotNull
        @field:Min(0)
        val y: Double?,

        @field:NotNull
        @field:OneToMany(
                mappedBy = "location",
                fetch = FetchType.LAZY
        )
        var plants: List<PlantEntity> = emptyList(),

        @field:Id
        @field:GeneratedValue
        val id: Long? = -1
)

