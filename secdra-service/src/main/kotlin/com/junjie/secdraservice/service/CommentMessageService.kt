package com.junjie.secdraservice.service

import com.junjie.secdradata.database.primary.entity.CommentMessage

interface CommentMessageService {
    fun save(commentMessage: CommentMessage): CommentMessage

    fun list(authorId: String): List<CommentMessage>

    fun countUnread(authorId: String): Long

    fun listUnread(authorId: String): List<CommentMessage>
    
    fun deleteByMonthAgo()
}