package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.Follow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface FollowDAO : JpaRepository<Follow, String> {
    fun existsByFollowerIdAndFollowingId(followerId: String, followingId: String): Boolean
    @Transactional
    fun deleteByFollowerIdAndFollowingId(followerId: String, followingId: String)

    fun findAllByFollowerId(followerId: String, pageable: Pageable): Page<Follow>

    fun findAllByFollowingId(followingId: String, pageable: Pageable): Page<Follow>
}