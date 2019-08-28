package com.junjie.secdraservice.serviceimpl

import com.junjie.secdradata.constant.FollowState
import com.junjie.secdradata.database.primary.dao.FollowDAO
import com.junjie.secdradata.database.primary.entity.Follow
import com.junjie.secdraservice.service.FollowService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FollowServiceImpl(private val followDAO: FollowDAO) : FollowService {
    override fun exists(followerId: String?, followingId: String): FollowState {
        if (followerId == null) {
            return FollowState.CONCERNED
        }
        if (followerId == followingId) {
            return FollowState.SElF
        }
        return try {
            if (followDAO.existsByFollowerIdAndFollowingId(followerId, followingId)) FollowState.CONCERNED else FollowState.STRANGE
        } catch (e: Exception) {
            FollowState.SElF
        }
    }

    override fun save(followerId: String, followingId: String): Follow {
        val follow = Follow(followerId, followingId)
        return followDAO.save(follow)
    }

    override fun remove(followerId: String, followingId: String): Boolean {
        return try {
            followDAO.deleteByFollowerIdAndFollowingId(followerId, followingId)
            true
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
