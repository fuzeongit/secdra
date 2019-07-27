package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.FollowMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface FollowMessageDAO : JpaRepository<FollowMessage, String> , JpaSpecificationExecutor<FollowMessage> {
    fun findAllByFollowingIdOrderByCreateDateDesc(followingId: String): List<FollowMessage>

    fun countByFollowingIdAndIsRead(followingId: String, isRead: Boolean): Long

    fun findAllByFollowingIdAndIsReadOrderByCreateDateDesc(followingId: String, isRead: Boolean): List<FollowMessage>
}