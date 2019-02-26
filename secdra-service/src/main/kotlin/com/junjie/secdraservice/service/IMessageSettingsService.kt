package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.MessageSettings

interface IMessageSettingsService {
    fun get(userId: String): MessageSettings

    fun save(messageSettings: MessageSettings): MessageSettings
}