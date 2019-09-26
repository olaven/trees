package org.olaven.enterprise.trees.misc;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

/**
 * Right now, Togglz does not seem to work with Kotlin.
 * Therefore, this class is written in Java instead.
 * The same applies to /controller/InfoPageController
 */
public enum FeatureToggles implements Feature {

    @Label("Displaying an info page")
    INFO_PAGE;


    public boolean isActive() {

        return FeatureContext.getFeatureManager().isActive(this);
    }
}

