package org.olaven.enterprise.trees

import org.olaven.enterprise.trees.repository.PlantRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DatabaseReset {

    @Autowired
    private lateinit var plantRepository: PlantRepository

    fun reset() {

        plantRepository.deleteAll()
    }
}