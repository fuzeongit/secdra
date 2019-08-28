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

    fun count(drawId: String): Long

    fun list(drawId: String):List<Comment>

    fun listTop4(drawId:String):List<Comment>

    fun paging(drawId: String, pageable: Pageable): Page<Comment>
}