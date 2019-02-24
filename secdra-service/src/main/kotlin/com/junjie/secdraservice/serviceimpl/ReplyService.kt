package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.IReplyDao
import com.junjie.secdraservice.model.Reply
import com.junjie.secdraservice.service.IReplyService
import org.springframework.stereotype.Service

@Service
class ReplyService(private val replyDao: IReplyDao) : IReplyService {
    override fun save(reply: Reply): Reply {
        return replyDao.save(reply)
    }

    override fun list(commentId: String): List<Reply> {
        return replyDao.findAllByCommentIdOrderByCreateDateDesc(commentId)
    }
}