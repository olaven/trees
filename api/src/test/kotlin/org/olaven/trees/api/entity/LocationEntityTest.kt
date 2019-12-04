package org.olaven.trees.api.entity

import org.hibernate.LazyInitializationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertTrue

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class LocationEntityTest: EntityTestBase() {

    @Test
    fun `Plants are lazily fetched by default`() {

        val dto = getLocationDTO()
        val entity = persistLocation(dto)
        repeat((0 until 5).count()) {
            persistPlant(getPlantDTO(location = entity))
        }

        val retrieved = locationRepository.findById(entity.id)
        assertTrue(retrieved.isPresent)
        assertThrows<LazyInitializationException> {
            retrieved.get().plants.count()
        }
    }
}