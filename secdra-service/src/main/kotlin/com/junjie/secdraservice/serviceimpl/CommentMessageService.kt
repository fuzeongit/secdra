package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.ICommentMessageDao
import com.junjie.secdraservice.model.CommentMessage
import com.junjie.secdraservice.service.ICommentMessageService
import org.springframework.stereotype.Service

@Service
class CommentMessageService(private val commentMessageDao: ICommentMessageDao) : ICommentMessageService {
    override fun save(commentMessage: CommentMessage): CommentMessage {
        return commentMessageDao.save(commentMessage)
    }

    override fun list(authorId: String): List<CommentMessage> {
        return commentMessageDao.findAllByAuthorIdOrderByCreateDateDesc(authorId)
    }

    override fun countUnread(authorId: String): Long {
        return commentMessageDao.countByAuthorIdAndIsRead(authorId, false)
    }

    override fun listUnread(authorId: String): List<CommentMessage> {
        return commentMessageDao.findAllByAuthorIdAndIsReadOrderByCreateDateDesc(authorId, false)
    }
}