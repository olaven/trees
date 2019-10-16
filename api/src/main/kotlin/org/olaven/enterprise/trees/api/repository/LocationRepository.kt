package org.olaven.enterprise.trees.api.repository

import org.olaven.enterprise.trees.api.entity.LocationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.transaction.Transactional

@Repository
interface LocationRepository: CrudRepository<LocationEntity, Long>, CustomLocationRepository

interface CustomLocationRepository {

    fun update(id: Long, x: Double, y: Double): Boolean
    fun getNextPage(n: Int, keysetId: Long?, fetchPlants: Boolean = false): List<LocationEntity>
    fun getRandom(): LocationEntity?
}


@Transactional
@Repository
open class LocationRepositoryImpl(
        private val entityManager: EntityManager
): CustomLocationRepository {

    override fun update(id: Long, x: Double, y: Double): Boolean {

        val entity = entityManager.find(LocationEntity::class.java, id) ?: return false

        entity.x = x
        entity.y = y //TODO: should I update locations?
        entity.timestamp = ZonedDateTime.now().toInstant().toEpochMilli()

        entityManager.persist(entity)
        return true
    }
    override fun getNextPage(size: Int, keysetId: Long?, fetchPlants: Boolean): List<LocationEntity> {


        require(!(size < 0 || size > 1000)) { "Invalid size: $size" }

        val query: TypedQuery<LocationEntity> = if (keysetId == null)
            entityManager
                    .createQuery("select location from LocationEntity location order by location.id desc", LocationEntity::class.java)
        else
            entityManager
                    .createQuery("select location from LocationEntity location where location.id < ?1 order by location.id desc", LocationEntity::class.java)
                    .setParameter(1, keysetId)
        query.maxResults = size

        //NOTE: plants are always fetched, even if lazy. I.e. `locations` already contains them.
        //TODO: make JPA respect FetchType.LAZY
        val locations =  query.resultList
        if (fetchPlants) locations.forEach { it.plants.size }
        return locations
    }

    override fun getRandom(): LocationEntity? {

        val total = (entityManager
                .createQuery("select count(location.id) from LocationEntity location")
                .singleResult as Long).toInt()

        if (total == 0) return null
        val selected = Random().nextInt(total)

        return entityManager
                .createQuery("select location from LocationEntity location", LocationEntity::class.java)
                .setFirstResult(selected)
                .setMaxResults(1)
                .singleResult;
    }

}