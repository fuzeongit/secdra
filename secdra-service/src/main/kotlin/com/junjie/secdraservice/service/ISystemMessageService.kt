package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.SystemMessage

interface ISystemMessageService {
    fun save(systemMessage: SystemMessage): SystemMessage

    fun list(userId: String): List<SystemMessage>

    fun countUnread(userId: String): Long

    fun listUnread(userId: String): List<SystemMessage>

    fun deleteByMonthAgo()
}