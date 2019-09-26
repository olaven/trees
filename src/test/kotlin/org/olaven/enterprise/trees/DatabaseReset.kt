package org.olaven.enterprise.trees

import org.olaven.enterprise.trees.repository.LocationRepository
import org.olaven.enterprise.trees.repository.PlantRepository
import org.springframework.stereotype.Service

@Service
class DatabaseReset(
        private val plantRepository: PlantRepository, 
        private val locationRepository: LocationRepository
) {

    fun reset() {

        plantRepository.deleteAll()
        locationRepository.deleteAll()
    }
}