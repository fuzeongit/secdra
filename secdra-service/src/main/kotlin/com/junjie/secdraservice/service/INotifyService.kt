package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Notify

interface INotifyService {
    fun save(notify: Notify): Notify

    fun list(receiveId: String): List<Notify>

    fun countUnread(receiveId: String): Long

    fun listUnread(receiveId: String): List<Notify>
}