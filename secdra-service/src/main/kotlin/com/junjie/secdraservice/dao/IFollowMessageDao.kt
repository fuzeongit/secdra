package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.FollowMessage
import org.springframework.data.jpa.repository.JpaRepository

interface IFollowMessageDao : JpaRepository<FollowMessage, String> {
    fun findAllByFollowingIdOrderByCreateDateDesc(followingId: String): List<FollowMessage>

    fun countByFollowingIdAndIsRead(followingId: String, isRead: Boolean): Long

    fun findAllByFollowingIdAndIsReadOrderByCreateDateDesc(followingId: String, isRead: Boolean): List<FollowMessage>
}