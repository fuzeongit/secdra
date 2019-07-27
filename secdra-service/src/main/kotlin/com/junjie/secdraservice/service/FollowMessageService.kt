package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.FollowMessage

interface FollowMessageService {
    fun save(followMessage: FollowMessage): FollowMessage

    fun list(followingId: String): List<FollowMessage>

    fun countUnread(followingId: String): Long

    fun listUnread(followingId: String): List<FollowMessage>

    fun deleteByMonthAgo()
}