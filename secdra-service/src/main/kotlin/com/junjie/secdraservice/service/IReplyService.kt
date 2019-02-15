package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Comment
import com.junjie.secdraservice.model.Reply
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * 回复的服务
 *
 * @author fjj
 */
interface IReplyService {
    fun save(reply : Reply): Reply

    fun listIsRead(authorId: String): List<Comment>

    fun countIsRead(authorId: String): Long

    fun pagingByDrawId(drawId: String, pageable: Pageable): Page<Comment>
}