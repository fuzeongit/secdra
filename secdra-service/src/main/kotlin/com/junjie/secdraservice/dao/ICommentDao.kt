package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ICommentDao : JpaRepository<Comment, String> {
    fun countByAuthorIdAndIsRead(authorId: String, unread: Boolean): Long

    fun findAllByAuthorIdAndIsRead(authorId: String, unread: Boolean): List<Comment>

    fun findAllByDrawId(drawId: String, pageable: Pageable): Page<Comment>
}