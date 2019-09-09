package org.olaven.enterprise.trees

import org.springframework.stereotype.Component
import org.togglz.core.Feature
import org.togglz.core.manager.TogglzConfig
import org.togglz.core.repository.StateRepository
import org.togglz.core.repository.file.FileBasedStateRepository
import org.togglz.core.user.UserProvider
import org.togglz.servlet.user.ServletUserProvider
import java.io.File


@Component
class TogglzConfiguration : TogglzConfig {

    override fun getFeatureClass(): Class<out Feature> {
        return TreeFeatures::class.java
    }

    override fun getStateRepository(): StateRepository {
        return FileBasedStateRepository(File("/tmp/features.properties"))
    }

    override fun getUserProvider(): UserProvider {
        return ServletUserProvider("admin")
    }
}