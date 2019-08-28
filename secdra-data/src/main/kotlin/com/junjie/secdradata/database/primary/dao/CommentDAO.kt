package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentDAO : JpaRepository<Comment, String> {
    fun countByDrawId(drawId: String): Long

    fun findAllByDrawIdOrderByCreateDateDesc(drawId: String): List<Comment>

    fun findAllByDrawId(drawId: String, pageable: Pageable): Page<Comment>
}