package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.CommentMessage
import org.springframework.data.jpa.repository.JpaRepository

interface ICommentMessageDao : JpaRepository<CommentMessage, String> {
    fun findAllByAuthorIdOrderByCreateDateDesc(authorId: String): List<CommentMessage>

    fun countByAuthorIdAndIsRead(authorId: String, isRead: Boolean): Long

    fun findAllByAuthorIdAndIsReadOrderByCreateDateDesc(authorId: String, isRead: Boolean): List<CommentMessage>
}