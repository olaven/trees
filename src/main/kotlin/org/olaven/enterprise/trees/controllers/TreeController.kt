package org.olaven.enterprise.trees.controllers

import org.olaven.enterprise.trees.entities.TreeEntity
import org.olaven.enterprise.trees.repositories.TreeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class TreeController {

    @Autowired
    private lateinit var treeRepository: TreeRepository;

    @GetMapping("/trees/{id}")
    fun getTree(@PathVariable id: Long) =
            treeRepository.findById(id)

    @PostMapping("trees")
    fun postTree(@RequestBody tree: TreeEntity) =
            treeRepository.save(tree)
}