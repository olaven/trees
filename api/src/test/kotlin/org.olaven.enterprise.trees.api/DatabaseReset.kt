package org.olaven.enterprise.trees.api

import org.olaven.enterprise.trees.api.repository.LocationRepository
import org.olaven.enterprise.trees.api.repository.PlantRepository
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