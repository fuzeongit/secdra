package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.util.DateUtil
import com.junjie.secdraservice.dao.ICommentMessageDao
import com.junjie.secdraservice.model.CommentMessage
import com.junjie.secdraservice.service.ICommentMessageService
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

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

    override fun deleteByMonthAgo() {
        val specification = Specification<CommentMessage> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            predicatesList.add(criteriaBuilder.greaterThan(root.get("createDate"), DateUtil.addDate(Date(), 0, -30, 0, 0, 0, 0, 0)))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        val list = commentMessageDao.findAll(specification)
        for (item in list) {
            commentMessageDao.delete(item)
        }
        return
    }
}