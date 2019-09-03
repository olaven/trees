package org.olaven.enterprise.trees.transformer

import org.olaven.enterprise.trees.dto.LocationDTO
import org.olaven.enterprise.trees.entity.LocationEntity
import org.olaven.enterprise.trees.repository.LocationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionException
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
internal class LocationTransformer: Transformer<LocationDTO, LocationEntity>() {

    @Autowired
    private lateinit var locationRepository: LocationRepository

    override fun toDTO(entity: LocationEntity) =
            LocationDTO(entity.x, entity.y, entity.id)

    override fun toEntity(dto: LocationDTO) =
        LocationEntity(
                x = dto.x,
                y = dto.y,
                id = dto.id
        )

}