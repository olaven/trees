package org.olaven.trees.api.transformer

import org.olaven.trees.api.dto.LocationDTO
import org.olaven.trees.api.dto.PlantDto
import org.olaven.trees.api.entity.PlantEntity
import org.olaven.trees.api.repository.LocationRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class PlantTransformer(
        private val locationRepository: LocationRepository
) {

    fun toDTO(entity: PlantEntity): PlantDto =
        PlantDto(
            entity.name, entity.description,
            entity.height, entity.age,
            LocationDTO(entity.location.point.x, entity.location.point.y, entity.location.id), entity.id
        )


    fun toEntity(dto: PlantDto): PlantEntity {

        require(dto.name != null && dto.description != null &&
                dto.age != null && dto.height != null && dto.location != null && dto.location!!.id != null) {"Plant DTO was invalid"}

        val location = locationRepository.findById(dto.location!!.id)
        require(location.isPresent) { "Could not find location with id ${dto.location!!.id}" }

        return PlantEntity(
                dto.name!!,
                dto.description!!,
                dto.height!!,
                dto.age!!,
                location.get(),
                timestamp = ZonedDateTime.now().toInstant().toEpochMilli()
        )
    }
}