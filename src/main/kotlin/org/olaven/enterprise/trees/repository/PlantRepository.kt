package org.olaven.enterprise.trees.repository

import org.olaven.enterprise.trees.entity.PlantEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Repository
interface PlantRepository: CrudRepository<PlantEntity, Long>, CustomPlantRepository {


}

interface CustomPlantRepository {

    fun update(id: Long, name: String, description: String, age: Int, height: Double): Boolean
}

@Transactional
@Repository
open class PlantRepositoryImpl: CustomPlantRepository {

    @Autowired
    private lateinit var entityManager: EntityManager

    override fun update(id: Long, name: String, description: String, age: Int, height: Double): Boolean {

        val entity = entityManager.find(PlantEntity::class.java, id) ?: return false

        entity.name = name
        entity.description = description
        entity.age = age
        entity.height = height

        entityManager.persist(entity)
        return true
    }
}
