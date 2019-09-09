package org.olaven.enterprise.trees;

import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

/**
 * Feature Toggles.
 *
 * Has to be a Java file, as there
 * are compilation errors when using
 * Kotlin https://github.com/togglz/togglz/issues/292
 */
public enum TreeFeatures implements Feature {

    @EnabledByDefault
    @Label("new welcome message")
    INFO_PAGE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}