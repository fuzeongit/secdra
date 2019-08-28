package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.SystemMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface SystemMessageDAO : JpaRepository<SystemMessage, String>, JpaSpecificationExecutor<SystemMessage> {
    fun findAllByUserIdOrderByCreateDateDesc(userId: String): List<SystemMessage>

    fun countByUserIdAndReview(userId: String, review: Boolean): Long

    fun findAllByUserIdAndReviewOrderByCreateDateDesc(userId: String, review: Boolean): List<SystemMessage>
}