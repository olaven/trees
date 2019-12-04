package org.olaven.trees.api.entity

import org.jetbrains.annotations.NotNull
import org.olaven.trees.api.misc.epochMilli
import org.springframework.data.geo.Point
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
open class LocationEntity (

        /*@field:NotNull
        @field:Min(0)
        @field:Max(90)
        var x: Double?,

        @field:NotNull
        @field:Min(-180)
        @field:Max(180)
        var y: Double?,*/

        @Column(columnDefinition = "geometry")
        var point: Point,

        @field:NotNull
        @field:OneToMany(
                mappedBy = "location",
                fetch = FetchType.LAZY
        )
        var plants: List<PlantEntity> = emptyList(),

        @field:Id
        @field:GeneratedValue
        val id: Long? = null,


        @field:NotNull
        @field:Min(0)
        var timestamp: Long = epochMilli(),

        @Version
        internal val version: Long = 0
)

