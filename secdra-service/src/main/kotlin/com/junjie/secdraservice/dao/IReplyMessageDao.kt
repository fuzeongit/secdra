package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.ReplyMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface IReplyMessageDao : JpaRepository<ReplyMessage, String>, JpaSpecificationExecutor<ReplyMessage> {
    fun findAllByCriticIdOrderByCreateDateDesc(criticId: String): List<ReplyMessage>

    fun countByCriticIdAndIsRead(criticId: String, isRead: Boolean): Long

    fun findAllByCriticIdAndIsReadOrderByCreateDateDesc(criticId: String, isRead: Boolean): List<ReplyMessage>
}