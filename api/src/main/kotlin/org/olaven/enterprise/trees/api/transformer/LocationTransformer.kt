package org.olaven.enterprise.trees.api.transformer

import org.olaven.enterprise.trees.api.dto.LocationDTO
import org.olaven.enterprise.trees.api.entity.LocationEntity
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class LocationTransformer(
        private val plantTransformer: PlantTransformer
) {

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
                id = dto.id,
                timestamp = ZonedDateTime.now().toInstant().toEpochMilli()
        )
}