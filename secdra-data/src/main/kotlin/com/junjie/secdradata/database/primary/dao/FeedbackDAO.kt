package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.Comment
import com.junjie.secdradata.database.primary.entity.Feedback
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface FeedbackDAO : JpaRepository<Feedback, String>, JpaSpecificationExecutor<Feedback> {
    fun findAllByEmail(email: String, pageable: Pageable): Page<Feedback>
}