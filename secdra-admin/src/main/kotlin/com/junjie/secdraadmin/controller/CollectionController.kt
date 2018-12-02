package com.junjie.secdraadmin.controller

import com.junjie.secdraservice.dao.ICollectionDao
import com.junjie.secdraservice.dao.IDrawDao
import com.junjie.secdraservice.dao.IUserDao
import com.junjie.secdraservice.model.Collection
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("collection")
class CollectionController(private var drawDao: IDrawDao, private var userDao: IUserDao, private var collectionDao: ICollectionDao) {
    @PostMapping("/init")
    fun init(): Any {
        val userList = userDao.findAll()
        val drawList = drawDao.findAll()
        for (user in userList) {
            val takeList = drawList.shuffled().take(50)
            for (take in takeList) {
                if (take.userId.isNullOrEmpty() || user.id.isNullOrEmpty() || take.userId == user.id || take.isPrivate) {
                    continue
                }
                if (collectionDao.existsByUserIdAndDrawId(user.id!!, take.id!!)) {
                    continue
                }
                val collection = Collection()
                collection.drawId = take.id
                collection.userId = user.id
                collectionDao.save(collection)
            }
        }
        for (draw in drawList) {
            draw.likeAmount = collectionDao.countByDrawId(draw.id!!)
            drawDao.save(draw)
        }
        return true
    }
}