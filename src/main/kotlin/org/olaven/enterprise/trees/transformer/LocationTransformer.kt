package org.olaven.enterprise.trees.transformer

import org.olaven.enterprise.trees.dto.LocationDTO
import org.olaven.enterprise.trees.entity.LocationEntity
import org.springframework.stereotype.Service

@Service
internal class LocationTransformer() {

    val plantTransformer = PlantTransformer()

    fun toDTO(entity: LocationEntity, includePlants: Boolean) =
            LocationDTO(entity.x, entity.y, entity.id, if (includePlants) {
                entity.plants.map { plantTransformer.toDTO(it) }
            } else {
                emptyList()
            })

    fun toEntity(dto: LocationDTO) =
        LocationEntity(
                x = dto.x,
                y = dto.y,
                id = dto.id
        )
}