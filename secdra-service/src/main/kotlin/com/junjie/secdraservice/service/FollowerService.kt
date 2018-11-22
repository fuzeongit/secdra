package com.junjie.secdraservice.service

import com.junjie.secdraservice.dao.IFollowerDao
import com.junjie.secdraservice.model.Follower
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FollowerService(private val followerDao: IFollowerDao) : IFollowerService {
    override fun exists(userId: String?, followerId: String): Boolean? {
        if (userId == followerId) {
            return null
        }
        return try {
            followerDao.existsByUserIdAndFollowerId(userId!!, followerId)
        } catch (e: Exception) {
            null
        }
    }

    override fun save(userId: String, followerId: String): Follower {
        val follower = Follower()
        follower.userId = userId
        follower.followerId = followerId
        return followerDao.save(follower)
    }

    override fun remove(userId: String, followerId: String): Boolean {
        try {
            followerDao.deleteByUserIdAndFollowerId(userId, followerId)
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    override fun paging(userId: String, pageable: Pageable): Page<Follower> {
        return followerDao.findAllByUserId(userId, pageable)
    }
}
