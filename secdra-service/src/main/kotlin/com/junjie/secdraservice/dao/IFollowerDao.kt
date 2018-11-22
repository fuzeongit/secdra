package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Follower
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface IFollowerDao : JpaRepository<Follower, String> {
    fun existsByUserIdAndFollowerId(userId: String, followerId: String): Boolean
    @Transactional
    fun deleteByUserIdAndFollowerId(userId: String, followerId: String)

    fun findAllByUserId(userId: String, pageable: Pageable): Page<Follower>
}