package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.SystemMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface SystemMessageDAO : JpaRepository<SystemMessage, String>, JpaSpecificationExecutor<SystemMessage> {
    fun findAllByUserIdOrderByCreateDateDesc(userId: String): List<SystemMessage>

    fun countByUserIdAndReview(userId: String, review: Boolean): Long

    fun findAllByUserIdAndReviewOrderByCreateDateDesc(userId: String, review: Boolean): List<SystemMessage>
}