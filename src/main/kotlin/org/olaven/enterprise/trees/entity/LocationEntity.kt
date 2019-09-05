package org.olaven.enterprise.trees.entity

import org.jetbrains.annotations.NotNull
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
class LocationEntity (

        @field:NotNull
        @field:Min(0)
        val x: Double? = -1.0,

        @field:NotNull
        @field:Min(0)
        val y: Double? = -1.0,

        @field:NotNull
        @field:OneToMany(
                mappedBy = "location",
                fetch = FetchType.LAZY
        )
        val plants: List<PlantEntity> = emptyList(),

        @field:Id
        @field:GeneratedValue
        val id: Long? = -1
)
