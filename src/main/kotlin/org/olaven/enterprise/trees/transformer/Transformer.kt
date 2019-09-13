package org.olaven.enterprise.trees.transformer

import org.olaven.enterprise.trees.dto.DTO

internal abstract class Transformer<D: DTO, E> {

    abstract fun toDTO(entity: E): D
    fun toDTOs(entities: List<E>) =
            entities.map { toDTO(it) }

    abstract fun toEntity(dto: D): E
    fun toEntities(dtos: List<D>) =
            dtos.map { toEntity(it) }
}