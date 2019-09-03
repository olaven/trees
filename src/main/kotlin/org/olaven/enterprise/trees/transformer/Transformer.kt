package org.olaven.enterprise.trees.transformer

internal abstract class Transformer<DTO, Entity> {

    abstract fun toDTO(entity: Entity): DTO
    abstract fun toEntity(dto: DTO): Entity
}