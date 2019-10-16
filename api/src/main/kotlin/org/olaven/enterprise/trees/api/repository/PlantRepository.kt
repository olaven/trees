package org.olaven.enterprise.trees.api.repository

import org.olaven.enterprise.trees.api.entity.PlantEntity
import org.olaven.enterprise.trees.api.entity.LocationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Repository
interface PlantRepository: CrudRepository<PlantEntity, Long>, CustomPlantRepository {


}

interface CustomPlantRepository {

    fun update(id: Long, name: String, description: String, age: Int, height: Double, location: LocationEntity): Boolean
}

@Transactional
@Repository
class PlantRepositoryImpl(
        private val entityManager: EntityManager
): CustomPlantRepository {

    override fun update(id: Long, name: String, description: String, age: Int, height: Double, location: LocationEntity): Boolean {

        val entity = entityManager.find(PlantEntity::class.java, id) ?: return false

        entity.name = name
        entity.description = description
        entity.age = age
        entity.height = height
        entity.location = location
        entity.timestamp = ZonedDateTime.now().toInstant().toEpochMilli()

        entityManager.persist(entity)
        return true
    }
}
