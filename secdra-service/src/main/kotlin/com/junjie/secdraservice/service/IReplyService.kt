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
    fun save(reply: Reply): Reply

    fun list(commentId: String): List<Reply>

    fun listUnread(criticId: String): List<Reply>

    fun countUnread(criticId: String): Long
}