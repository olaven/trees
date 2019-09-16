package org.olaven.enterprise.trees.transformer

import org.olaven.enterprise.trees.dto.LocationDTO
import org.olaven.enterprise.trees.dto.PlantDto
import org.olaven.enterprise.trees.entity.PlantEntity
import org.olaven.enterprise.trees.repository.LocationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
internal class PlantTransformer: Transformer<PlantDto, PlantEntity>() {

    @Autowired
    private lateinit var locationRepository: LocationRepository

    override fun toDTO(entity: PlantEntity): PlantDto =
        PlantDto(
            entity.name, entity.description,
            entity.height, entity.age,
            LocationDTO(entity.location.x, entity.location.y, entity.location.id), entity.id
        )


    override fun toEntity(dto: PlantDto): PlantEntity {

        require(dto.name != null && dto.description != null &&
                dto.age != null && dto.height != null && dto.location != null && dto.location!!.id != null) {"Plant DTO was invalid"}

        val location = locationRepository.findById(dto.location!!.id)
        require(location.isPresent) { "Could not find location with id ${dto.location!!.id}" }

        return PlantEntity(dto.name!!, dto.description!!, dto.height!!, dto.age!!, location.get());
    }
}