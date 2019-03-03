package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Reply

/**
 * 回复的服务
 *
 * @author fjj
 */
interface IReplyService {
    fun save(reply: Reply): Reply

    fun list(commentId: String): List<Reply>
}