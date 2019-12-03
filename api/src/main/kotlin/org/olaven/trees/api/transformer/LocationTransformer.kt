package org.olaven.trees.api.transformer

import org.olaven.trees.api.dto.LocationDTO
import org.olaven.trees.api.entity.LocationEntity
import org.springframework.data.geo.Point
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class LocationTransformer(
        private val plantTransformer: PlantTransformer
) {

    fun toDTO(entity: LocationEntity, includePlants: Boolean) =
            LocationDTO(entity.point.x, entity.point.y, entity.id, if (includePlants) {
                entity.plants.map { plantTransformer.toDTO(it) }
            } else {
                emptyList()
            })

    fun toEntity(dto: LocationDTO) =
        LocationEntity(
                point = Point(dto.x!!, dto.y!!),
                id = dto.id,
                timestamp = ZonedDateTime.now().toInstant().toEpochMilli()
        )
}