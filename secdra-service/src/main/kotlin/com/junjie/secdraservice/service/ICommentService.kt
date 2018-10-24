package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


/**
 * 评论的服务
 *
 * @author fjj
 */
interface ICommentService {
    fun save(userId: String, drawId: String, content: String): Comment

    fun pagingUnread(userId: String, pageable: Pageable): Page<Comment>

    fun pagingByDrawId(drawId: String, pageable: Pageable): Page<Comment>
}