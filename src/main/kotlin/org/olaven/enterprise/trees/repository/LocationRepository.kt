package org.olaven.enterprise.trees.repository

import org.olaven.enterprise.trees.entity.LocationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.transaction.Transactional

@Repository
interface LocationRepository: CrudRepository<LocationEntity, Long>, CustomLocationRepository

interface CustomLocationRepository {

    fun getNextPage(n: Int, keysetId: Long?, fetchPlants: Boolean = false): List<LocationEntity>
    fun getRandom(): LocationEntity?
}


@Transactional
@Repository
open class LocationRepositoryImpl(
        private val entityManager: EntityManager
): CustomLocationRepository {

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

        val total = entityManager
                .createQuery("select count(location) from LocationEntity location")
                .firstResult

        if (total == 0) return null
        val selected = Random().nextInt(total) + 1

        return entityManager
                .createQuery("select location from LocationEntity location", LocationEntity::class.java)
                .setFirstResult(selected)
                .setMaxResults(1)
                .singleResult;
    }

}