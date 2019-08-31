package org.olaven.enterprise.trees.transformer

abstract class Transformer<DTO, Entity> {

    abstract fun toDTO(entity: Entity): DTO

    abstract fun toEntity(dto: DTO): Entity

    fun toDTOs(entities: Iterable<Entity>) =
            entities.map { toDTO(entity = it) }

    fun toEntities(DTOs: Iterable<DTO>) =
            DTOs.map { toEntity(dto = it) }
}