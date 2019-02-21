package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Reply
import org.springframework.data.jpa.repository.JpaRepository

interface IReplyDao : JpaRepository<Reply, String> {
    fun countByCriticIdAndIsRead(criticId: String, unread: Boolean): Long

    fun findAllByCriticIdAndIsReadOrderByCreateDateDesc(criticId: String, unread: Boolean): List<Reply>

    fun findAllByCommentIdOrderByCreateDateDesc(commentId: String): List<Reply>
}