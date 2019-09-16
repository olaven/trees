package org.olaven.enterprise.trees.transformer

import org.olaven.enterprise.trees.dto.LocationDTO
import org.olaven.enterprise.trees.entity.LocationEntity
import org.springframework.stereotype.Service

@Service
internal class LocationTransformer: Transformer<LocationDTO, LocationEntity>() {

    val plantTransformer = PlantTransformer()

    override fun toDTO(entity: LocationEntity) =
            LocationDTO(entity.x, entity.y, entity.id, entity.plants.map { plantTransformer.toDTO(it) })

    override fun toEntity(dto: LocationDTO) =
        LocationEntity(
                x = dto.x,
                y = dto.y,
                id = dto.id
        )
}