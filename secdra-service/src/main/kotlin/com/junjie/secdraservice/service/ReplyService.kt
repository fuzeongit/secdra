package com.junjie.secdraservice.service

import com.junjie.secdraservice.dao.IReplyDao
import com.junjie.secdraservice.model.Reply
import org.springframework.stereotype.Service

@Service
class ReplyService(private val replyDao: IReplyDao) : IReplyService {
    override fun save(reply: Reply): Reply {
        return replyDao.save(reply)
    }

    override fun list(commentId: String): List<Reply> {
        return replyDao.findAllByCommentIdOrderByCreateDateDesc(commentId)
    }

    override fun listUnread(criticId: String): List<Reply> {
        return replyDao.findAllByCriticIdAndIsReadOrderByCreateDateDesc(criticId, false)
    }

    override fun countUnread(criticId: String): Long {
        return replyDao.countByCriticIdAndIsRead(criticId, false)
    }
}