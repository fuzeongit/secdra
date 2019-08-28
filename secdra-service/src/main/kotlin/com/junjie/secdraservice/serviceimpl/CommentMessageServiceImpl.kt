package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.util.DateUtil
import com.junjie.secdradata.database.primary.dao.CommentMessageDAO
import com.junjie.secdradata.database.primary.entity.CommentMessage
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
        return commentMessageDAO.countByAuthorIdAndReview(authorId, false)
    }

    override fun listUnread(authorId: String): List<CommentMessage> {
        return commentMessageDAO.findAllByAuthorIdAndReviewOrderByCreateDateDesc(authorId, false)
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