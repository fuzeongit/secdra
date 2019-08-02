package com.junjie.secdraservice.service

import com.junjie.secdraservice.constant.FollowState
import com.junjie.secdraservice.model.Follow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * 用户的关注服务
 *
 * @author fjj
 */

interface FollowService {
    fun exists(followerId: String?, followingId: String): FollowState

    fun save(followerId: String, followingId: String): Follow

    fun remove(followerId: String, followingId: String): Boolean

    fun pagingByFollowerId(followerId: String, pageable: Pageable): Page<Follow>

    fun pagingByFollowingId(followingId: String, pageable: Pageable): Page<Follow>
}