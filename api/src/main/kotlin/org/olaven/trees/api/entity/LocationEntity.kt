package org.olaven.trees.api.entity

import org.jetbrains.annotations.NotNull
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
open class LocationEntity (

        @field:NotNull
        @field:Min(0)
        var x: Double?,

        @field:NotNull
        @field:Min(0)
        var y: Double?,

        @field:NotNull
        @field:OneToMany(
                mappedBy = "location",
                fetch = FetchType.LAZY
        )
        var plants: List<PlantEntity> = emptyList(),

        @field:Id
        @field:GeneratedValue
        val id: Long? = -1,


        @field:NotNull
        @field:Min(0)
        var timestamp: Long,

        @Version
        internal val version: Long = 0
)

