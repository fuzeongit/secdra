package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.IFollowDao
import com.junjie.secdraservice.model.Follow
import com.junjie.secdraservice.service.IFollowService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FollowService(private val followDao: IFollowDao) : IFollowService {
    override fun exists(followerId: String?, followingId: String): Boolean? {
        if (followerId == followingId) {
            return null
        }
        return try {
            followDao.existsByFollowerIdAndFollowingId(followerId!!, followingId)
        } catch (e: Exception) {
            null
        }
    }

    override fun save(followerId: String, followingId: String): Follow {
        val follow = Follow()
        follow.followerId = followerId
        follow.followingId = followingId
        return followDao.save(follow)
    }

    override fun remove(followerId: String, followingId: String): Boolean {
        try {
            followDao.deleteByFollowerIdAndFollowingId(followerId, followingId)
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    override fun paging(followerId: String, pageable: Pageable): Page<Follow> {
        return followDao.findAllByFollowerId(followerId, pageable)
    }
}
