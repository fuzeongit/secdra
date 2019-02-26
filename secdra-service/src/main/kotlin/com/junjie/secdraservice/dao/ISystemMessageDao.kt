package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.SystemMessage
import org.springframework.data.jpa.repository.JpaRepository

interface ISystemMessageDao : JpaRepository<SystemMessage, String> {
    fun findAllByUserIdOrderByCreateDateDesc(userId: String): List<SystemMessage>

    fun countByUserIdAndIsRead(userId: String, isRead: Boolean): Long

    fun findAllByUserIdAndIsReadOrderByCreateDateDesc(userId: String, isRead: Boolean): List<SystemMessage>
}