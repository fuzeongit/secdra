package com.junjie.secdraservice.service

import com.junjie.secdradata.database.primary.entity.ReplyMessage

interface ReplyMessageService {
    fun save(replyMessage: ReplyMessage): ReplyMessage

    fun list(criticId: String): List<ReplyMessage>

    fun countUnread(criticId: String): Long

    fun listUnread(criticId: String): List<ReplyMessage>

    fun deleteByMonthAgo()
}