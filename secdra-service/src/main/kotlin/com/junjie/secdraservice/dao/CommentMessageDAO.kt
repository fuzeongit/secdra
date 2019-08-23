package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.CommentMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface CommentMessageDAO : JpaRepository<CommentMessage, String>, JpaSpecificationExecutor<CommentMessage> {
    fun findAllByAuthorIdOrderByCreateDateDesc(authorId: String): List<CommentMessage>

    fun countByAuthorIdAndRead(authorId: String, read: Boolean): Long

    fun findAllByAuthorIdAndReadOrderByCreateDateDesc(authorId: String, read: Boolean): List<CommentMessage>
}