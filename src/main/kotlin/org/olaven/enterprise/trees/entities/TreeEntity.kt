package org.olaven.enterprise.trees.entities

import org.jetbrains.annotations.NotNull
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size
import kotlin.math.max
import kotlin.math.min

@Entity
data class TreeEntity (

    @NotEmpty
    @Size(max = 100)
    val name: String,

    @Min(0)
    val height: Double,

    @NotEmpty
    @Size(max = 500)
    val description: String,

    @NotNull
    @Min(0)
    @Max(15_000)
    val age: Int,

    @Id
    @GeneratedValue
    val id: Long = 0
)