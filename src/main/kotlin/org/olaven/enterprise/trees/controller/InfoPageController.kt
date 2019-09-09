package org.olaven.enterprise.trees.controller

import org.olaven.enterprise.trees.TreeFeatures
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping(value = ["/info"])
class InfoPageController {

    @GetMapping("/")
    fun getInfoPage(): String {

        return if (TreeFeatures.INFO_PAGE.isActive) {

            "This is the info page etc. etc"
        } else {

            "Info page is not enabled.."
        }
    }
}