package com.junjie.secdraservice.service

import com.junjie.secdradata.database.primary.entity.Reply

/**
 * 回复的服务
 *
 * @author fjj
 */
interface ReplyService {
    fun save(reply: Reply): Reply

    fun list(commentId: String): List<Reply>
}