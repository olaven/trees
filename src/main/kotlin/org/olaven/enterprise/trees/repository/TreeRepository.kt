package org.olaven.enterprise.trees.repository

import org.olaven.enterprise.trees.entity.TreeEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TreeRepository: CrudRepository<TreeEntity, Long> {


}