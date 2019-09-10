package org.olaven.enterprise.trees.repository

import org.olaven.enterprise.trees.entity.LocationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository: CrudRepository<LocationEntity, Long>