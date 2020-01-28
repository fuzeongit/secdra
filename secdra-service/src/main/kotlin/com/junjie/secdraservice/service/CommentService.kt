package com.junjie.secdraservice.service

import com.junjie.secdradata.database.primary.entity.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


/**
 * 评论的服务
 *
 * @author fjj
 */
interface CommentService {
    fun save(comment: Comment): Comment

    fun count(pictureId: String): Long

    fun list(pictureId: String): List<Comment>

    fun listTop4(pictureId: String): List<Comment>

    fun paging(pictureId: String, pageable: Pageable): Page<Comment>
}