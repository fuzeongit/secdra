package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdraservice.dao.MessageSettingsDAO
import com.junjie.secdraservice.model.MessageSettings
import com.junjie.secdraservice.service.MessageSettingsService
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.stereotype.Service

@Service
class MessageSettingsServiceImpl(private val messageSettingsDAO: MessageSettingsDAO) : MessageSettingsService {
    @Cacheable("message::settings::get", key = "#userId")
    override fun get(userId: String): MessageSettings {
        val query = MessageSettings(userId)
        val matcher = ExampleMatcher.matching()
                .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id")
                .withIgnorePaths("commentStatus")
                .withIgnorePaths("replyStatus")
                .withIgnorePaths("followStatus")
                .withIgnorePaths("systemStatus")
                .withIgnorePaths("createDate")
                .withIgnorePaths("modifiedDate")
        val example = Example.of(query, matcher)
        return try {
            messageSettingsDAO.findOne(example).orElseThrow { NotFoundException("找不到消息配置") }
        } catch (e: NotFoundException) {
            val newSettings = MessageSettings(userId)
            messageSettingsDAO.save(newSettings)
        }
    }

    @CachePut("message::settings::get", key = "#messageSettings.userId")
    override fun save(messageSettings: MessageSettings): MessageSettings {
        return messageSettingsDAO.save(messageSettings)
    }
}