package org.olaven.enterprise.trees.transformer

import org.olaven.enterprise.trees.dto.PlantDto
import org.olaven.enterprise.trees.entity.PlantEntity

class PlantTransformer: Transformer<PlantDto, PlantEntity>() {

    override fun toDTO(entity: PlantEntity) =
        PlantDto(
            entity.name, entity.description,
            entity.height, entity.age,
            entity.id
        )


    override fun toEntity(dto: PlantDto): PlantEntity {

        class ConversionException: Exception();
        return if (dto.name == null  || dto.description == null ||
                dto.age == null || dto.height == null) throw ConversionException()
        else PlantEntity(dto.name!!, dto.description, dto.height, dto.age);
    }
}