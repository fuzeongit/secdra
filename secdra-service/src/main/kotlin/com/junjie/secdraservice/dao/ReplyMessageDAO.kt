package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.ReplyMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ReplyMessageDAO : JpaRepository<ReplyMessage, String>, JpaSpecificationExecutor<ReplyMessage> {
    fun findAllByCriticIdOrderByCreateDateDesc(criticId: String): List<ReplyMessage>

    fun countByCriticIdAndReview(criticId: String, review: Boolean): Long

    fun findAllByCriticIdAndReviewOrderByCreateDateDesc(criticId: String, review: Boolean): List<ReplyMessage>
}