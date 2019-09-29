package org.olaven.enterprise.trees.controller;

import io.swagger.annotations.Api;
import org.olaven.enterprise.trees.misc.FeatureToggles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.manager.FeatureManager;

/*
 * Right now, Togglz does not seem to work with Kotlin.
 * Therefore, this class is written in Java instead.
 * The same applies to /controller/FeatureToggles
*/

@RestController
@Api(value ="info", description = "Page with information")
@RequestMapping(value = {"info"})
public class InfoPageController {

    private FeatureManager featureManager;

    public InfoPageController(FeatureManager featureManager) {

        this.featureManager = featureManager;
    }

    @GetMapping(value = "")
    public String getInfoPage() {

        if (featureManager.isActive(FeatureToggles.INFO_PAGE)) {

            return "This is the new info page!";
        } else {

            return "The info page is not enabled yet..";
        }
    }
}
