package com.junjie.secdraadmin.controller

import com.junjie.secdraservice.dao.IFollowerDao
import com.junjie.secdraservice.dao.IUserDao
import com.junjie.secdraservice.model.Follower
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("follower")
class FollowerController(private var userDao: IUserDao, private var followerDao: IFollowerDao) {
    @PostMapping("/init")
    fun init(): Any {
        val userList = userDao.findAll()
        for (user in userList) {
            val takeList = userList.shuffled().take(50)
            for (take in takeList) {
                if (take.id.isNullOrEmpty() || user.id.isNullOrEmpty() || take.id == user.id) {
                    continue
                }
                if (followerDao.existsByUserIdAndFollowerId(user.id!!, take.id!!)) {
                    continue
                }
                val follower = Follower()
                follower.followerId = take.id
                follower.userId = user.id
                followerDao.save(follower)
            }
        }
        return true
    }
}