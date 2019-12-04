package org.olaven.trees.api.repository

import org.olaven.trees.api.entity.LocationEntity
import org.springframework.data.geo.Point
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.transaction.Transactional

@Repository
interface LocationRepository : CrudRepository<LocationEntity, Long>, CustomLocationRepository {

    //@Query(value = "SELECT *, (6371 * acos(cos(radians(?1)) *cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin(radians(?1)) * sin(radians(latitude)))) AS distance FROM location_entity HAVING distance < 20 ORDER BY distance LIMIT 0 , 20", nativeQuery = true)
    //fun findByPointNear(point: Point, distance: Distance): GeoResults<LocationEntity>
}

interface CustomLocationRepository {

    fun getNextPage(n: Int, keysetId: Long?, fetchPlants: Boolean = false): List<LocationEntity>
    fun getNextCenterPage(radius: Int, lat: Double, long: Double, fetchPlants: Boolean = false): List<LocationEntity>

    fun update(id: Long, x: Double, y: Double): Boolean
    fun getRandom(): LocationEntity?
}


@Transactional
@Repository
class LocationRepositoryImpl(
        private val entityManager: EntityManager
) : CustomLocationRepository {


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
        val locations = query.resultList
        if (fetchPlants) locations.forEach { it.plants.size }
        return locations
    }

    override fun getNextCenterPage(radius: Int, lat: Double, long: Double, fetchPlants: Boolean): List<LocationEntity> {

        val center = Point(lat, long)
        return entityManager.createQuery("select location from LocationEntity location where dwithin(location.point, :center, :radius) = true", LocationEntity::class.java)
                .setParameter("radius", radius)
                .setParameter("center", center)
                .resultList
                .toList()
    }

    override fun update(id: Long, x: Double, y: Double): Boolean {

        val entity = entityManager.find(LocationEntity::class.java, id) ?: return false

        /*entity.location.x = x
        entity.location.y = y //TODO: should I update locations?*/
        entity.point = Point(x, y)
        entity.timestamp = ZonedDateTime.now().toInstant().toEpochMilli()

        entityManager.persist(entity)
        return true
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