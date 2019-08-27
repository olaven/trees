package org.olaven.enterprise.trees.repository

import org.olaven.enterprise.trees.entity.PlantEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PlantRepostory: CrudRepository<PlantEntity, Long> {


}