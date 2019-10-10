package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdradata.database.primary.dao.FeedbackDAO
import com.junjie.secdradata.database.primary.entity.Feedback
import com.junjie.secdraservice.service.FeedbackService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FeedbackServiceImpl(private val feedbackDAO: FeedbackDAO) : FeedbackService {
    override fun get(id: String): Feedback {
        return feedbackDAO.findById(id).orElseThrow { NotFoundException("反馈信息不存在") }
    }

    override fun save(feedback: Feedback): Feedback {
        return feedbackDAO.save(feedback)
    }

    override fun paging(email: String?, pageable: Pageable): Page<Feedback> {
        return if (email.isNullOrBlank()) {
            feedbackDAO.findAll(pageable)
        } else {
            feedbackDAO.findAllByEmail(email!!, pageable)
        }
    }
}