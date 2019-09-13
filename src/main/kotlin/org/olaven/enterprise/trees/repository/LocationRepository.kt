package org.olaven.enterprise.trees.repository

import org.olaven.enterprise.trees.entity.LocationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.transaction.Transactional

@Repository
interface LocationRepository: CrudRepository<LocationEntity, Long>, CustomLocationRepository

interface CustomLocationRepository {

    fun getNextPage(n: Int, keysetId: Long?): List<LocationEntity>
}


@Transactional
@Repository
open class LocationRepositoryImpl(
        private val entityManager: EntityManager
): CustomLocationRepository {

    override fun getNextPage(size: Int, keysetId: Long?): List<LocationEntity> {

        require(!(size < 0 || size > 1000)) { "Invalid size: $size" }

        val query: TypedQuery<LocationEntity> = if (keysetId == null)
            entityManager
                    .createQuery("select location from LocationEntity location order by location.id desc", LocationEntity::class.java)
        else
            entityManager
                    .createQuery("select location from LocationEntity location where location.id < :keysetId order by location.id desc", LocationEntity::class.java)
                    .setParameter("keysetId", keysetId)



        query.maxResults = size

        return query.resultList
    }
}