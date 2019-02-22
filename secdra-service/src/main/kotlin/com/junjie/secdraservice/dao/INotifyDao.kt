package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Notify
import org.springframework.data.jpa.repository.JpaRepository

interface INotifyDao : JpaRepository<Notify, String> {
    fun findAllByReceiveIdOrderByCreateDateDesc(receiveId: String): List<Notify>

    fun countByReceiveIdAndIsRead(receiveId: String, isRead: Boolean): Long

    fun findAllByReceiveIdAndIsReadOrderByCreateDateDesc(receiveId: String, isRead: Boolean): List<Notify>
}