package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.SystemMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface SystemMessageDAO : JpaRepository<SystemMessage, String>, JpaSpecificationExecutor<SystemMessage> {
    fun findAllByUserIdOrderByCreateDateDesc(userId: String): List<SystemMessage>

    fun countByUserIdAndRead(userId: String, read: Boolean): Long

    fun findAllByUserIdAndReadOrderByCreateDateDesc(userId: String, read: Boolean): List<SystemMessage>
}