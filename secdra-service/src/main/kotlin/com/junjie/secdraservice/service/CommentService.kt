package com.junjie.secdraservice.service

import com.junjie.secdraservice.dao.ICommentDao
import com.junjie.secdraservice.model.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CommentService(private val commentDao: ICommentDao) : ICommentService {
    override fun save(comment: Comment): Comment {
        return commentDao.save(comment)
    }

    override fun listUnread(authorId: String): List<Comment> {
        return commentDao.findAllByAuthorIdAndIsRead(authorId, false)
    }

    override fun countUnread(authorId: String): Long {
        return commentDao.countByAuthorIdAndIsRead(authorId, false)
    }

    override fun pagingByDrawId(drawId: String, pageable: Pageable): Page<Comment> {
        return commentDao.findAllByDrawId(drawId, pageable)
    }
}