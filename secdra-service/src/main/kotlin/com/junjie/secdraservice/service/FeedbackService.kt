package com.junjie.secdraservice.service

import com.junjie.secdradata.database.primary.entity.Feedback
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
interface FeedbackService {
    fun get(id: String): Feedback

    fun save(feedback: Feedback): Feedback

    fun paging(email: String?, pageable: Pageable): Page<Feedback>
}