package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Follow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface FollowDAO : JpaRepository<Follow, String> {
    fun existsByFollowerIdAndFollowingId(followerId: String, followingId: String): Boolean
    @Transactional
    fun deleteByFollowerIdAndFollowingId(followerId: String, followingId: String)

    fun findAllByFollowerId(followerId: String, pageable: Pageable): Page<Follow>

    fun findAllByFollowingId(followingId: String, pageable: Pageable): Page<Follow>
}