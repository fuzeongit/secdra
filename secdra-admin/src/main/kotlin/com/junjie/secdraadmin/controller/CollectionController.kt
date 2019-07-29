package com.junjie.secdraadmin.controller

import com.junjie.secdraservice.dao.CollectionDAO
import com.junjie.secdraservice.dao.DrawDAO
import com.junjie.secdraservice.dao.UserDAO
import com.junjie.secdraservice.model.Collection
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("collection")
class CollectionController(private var drawDAO: DrawDAO, private var userDAO: UserDAO, private var collectionDAO: CollectionDAO) {
    @PostMapping("/init")
    fun init(): Any {
        val userList = userDAO.findAll()
        val drawList = drawDAO.findAll()
        for (user in userList) {
            val takeList = drawList.shuffled().take(50)
            for (take in takeList) {
                if (take.userId.isNullOrEmpty() || user.id.isNullOrEmpty() || take.userId == user.id || take.isPrivate) {
                    continue
                }
                if (collectionDAO.existsByUserIdAndDrawId(user.id!!, take.id!!)) {
                    continue
                }
                val collection = Collection()
                collection.drawId = take.id
                collection.userId = user.id
                collectionDAO.save(collection)
            }
        }
        for (draw in drawList) {
            draw.likeAmount = collectionDAO.countByDrawId(draw.id!!)
            drawDAO.save(draw)
        }
        return true
    }
}