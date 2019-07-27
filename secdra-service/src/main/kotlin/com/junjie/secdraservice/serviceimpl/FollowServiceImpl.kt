package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.FollowDAO
import com.junjie.secdraservice.model.Follow
import com.junjie.secdraservice.service.FollowService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FollowServiceImpl(private val followDAO: FollowDAO) : FollowService {
    override fun exists(followerId: String?, followingId: String): Boolean? {
        if (followerId == followingId) {
            return null
        }
        return try {
            followDAO.existsByFollowerIdAndFollowingId(followerId!!, followingId)
        } catch (e: Exception) {
            null
        }
    }

    override fun save(followerId: String, followingId: String): Follow {
        val follow = Follow()
        follow.followerId = followerId
        follow.followingId = followingId
        return followDAO.save(follow)
    }

    override fun remove(followerId: String, followingId: String): Boolean {
        try {
            followDAO.deleteByFollowerIdAndFollowingId(followerId, followingId)
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    override fun pagingByFollowerId(followerId: String, pageable: Pageable): Page<Follow> {
        return followDAO.findAllByFollowerId(followerId, pageable)
    }

    override fun pagingByFollowingId(followingId: String, pageable: Pageable): Page<Follow> {
        return followDAO.findAllByFollowingId(followingId, pageable)
    }
}
