package com.junjie.secdraservice.service

import com.junjie.secdradata.database.primary.entity.MessageSettings

interface MessageSettingsService {
    fun get(userId: String): MessageSettings

    fun save(messageSettings: MessageSettings): MessageSettings
}