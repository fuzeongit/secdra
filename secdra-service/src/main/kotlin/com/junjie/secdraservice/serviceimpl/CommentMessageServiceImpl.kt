package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.util.DateUtil
import com.junjie.secdraservice.dao.CommentMessageDAO
import com.junjie.secdraservice.model.CommentMessage
import com.junjie.secdraservice.service.CommentMessageService
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class CommentMessageServiceImpl(private val commentMessageDAO: CommentMessageDAO) : CommentMessageService {

    override fun save(commentMessage: CommentMessage): CommentMessage {
        return commentMessageDAO.save(commentMessage)
    }

    override fun list(authorId: String): List<CommentMessage> {
        return commentMessageDAO.findAllByAuthorIdOrderByCreateDateDesc(authorId)
    }

    override fun countUnread(authorId: String): Long {
        return commentMessageDAO.countByAuthorIdAndIsRead(authorId, false)
    }

    override fun listUnread(authorId: String): List<CommentMessage> {
        return commentMessageDAO.findAllByAuthorIdAndIsReadOrderByCreateDateDesc(authorId, false)
    }

    override fun deleteByMonthAgo() {
        val specification = Specification<CommentMessage> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            predicatesList.add(criteriaBuilder.greaterThan(root.get("createDate"), DateUtil.addDate(Date(), 0, -30, 0, 0, 0, 0, 0)))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        val list = commentMessageDAO.findAll(specification)
        for (item in list) {
            commentMessageDAO.delete(item)
        }
        return
    }
}