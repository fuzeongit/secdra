package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.Reply
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReplyDAO : JpaRepository<Reply, String> {
    fun findAllByCommentIdOrderByCreateDateDesc(commentId: String): List<Reply>
}