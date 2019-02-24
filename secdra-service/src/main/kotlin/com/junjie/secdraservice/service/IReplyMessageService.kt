package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.ReplyMessage

interface IReplyMessageService {
    fun save(replyMessage: ReplyMessage): ReplyMessage

    fun list(criticId: String): List<ReplyMessage>

    fun countUnread(criticId: String): Long

    fun listUnread(criticId: String): List<ReplyMessage>
}