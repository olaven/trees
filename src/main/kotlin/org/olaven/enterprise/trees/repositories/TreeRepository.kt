package org.olaven.enterprise.trees.repositories

import org.olaven.enterprise.trees.entities.TreeEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TreeRepository: CrudRepository<TreeEntity, Long> {


}