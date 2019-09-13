package org.olaven.enterprise.trees

import org.olaven.enterprise.trees.entity.LocationEntity
import org.olaven.enterprise.trees.repository.LocationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
@Profile(value = ["local"])
class DefaultDataInitializer {


    @Autowired
    lateinit var locationRepository: LocationRepository;

    @PostConstruct
    fun initialize() {

        val locations = getLocations()
        locationRepository.saveAll(locations)
    }

    //TODO: add plants on locations
    private fun getLocations(): List<LocationEntity> = listOf(
            LocationEntity(59.9838, 10.7256, emptyList()),
            LocationEntity(61.1488, 10.3743, emptyList()),
            LocationEntity(61.9216, 7.6694, emptyList()),
            LocationEntity(59.0539, 9.7275, emptyList())
    )
}