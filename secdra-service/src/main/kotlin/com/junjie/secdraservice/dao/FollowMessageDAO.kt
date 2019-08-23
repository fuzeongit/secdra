package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.FollowMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface FollowMessageDAO : JpaRepository<FollowMessage, String> , JpaSpecificationExecutor<FollowMessage> {
    fun findAllByFollowingIdOrderByCreateDateDesc(followingId: String): List<FollowMessage>

    fun countByFollowingIdAndReview(followingId: String, review: Boolean): Long

    fun findAllByFollowingIdAndReviewOrderByCreateDateDesc(followingId: String, review: Boolean): List<FollowMessage>
}