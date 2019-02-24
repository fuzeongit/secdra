package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.IReplyMessageDao
import com.junjie.secdraservice.model.ReplyMessage
import com.junjie.secdraservice.service.IReplyMessageService
import org.springframework.stereotype.Service

@Service
class ReplyMessageService(private val replyMessageDao: IReplyMessageDao) : IReplyMessageService {
    override fun save(replyMessage: ReplyMessage): ReplyMessage {
        return replyMessageDao.save(replyMessage)
    }

    override fun list(criticId: String): List<ReplyMessage> {
        return replyMessageDao.findAllByCriticIdOrderByCreateDateDesc(criticId)
    }

    override fun countUnread(criticId: String): Long {
        return replyMessageDao.countByCriticIdAndIsRead(criticId, false)
    }

    override fun listUnread(criticId: String): List<ReplyMessage> {
        return replyMessageDao.findAllByCriticIdAndIsReadOrderByCreateDateDesc(criticId, false)
    }
}