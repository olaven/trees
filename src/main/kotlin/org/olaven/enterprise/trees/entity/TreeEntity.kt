package org.olaven.enterprise.trees.entity

import org.jetbrains.annotations.NotNull
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
data class TreeEntity (

    @NotEmpty
    @Size(max = 100)
    val name: String,

    @NotEmpty
    @Size(max = 500)
    val description: String,

    @Min(0)
    val height: Double,

    @NotNull
    @Min(0)
    @Max(15_000)
    val age: Int,

    @Id
    @GeneratedValue
    val id: Long = 0
)