package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentDAO : JpaRepository<Comment, String> {
    fun countByPictureId(pictureId: String): Long

    fun findAllByPictureIdOrderByCreateDateDesc(pictureId: String): List<Comment>

    fun findAllByPictureId(pictureId: String, pageable: Pageable): Page<Comment>
}