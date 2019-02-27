package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.IMessageSettingsDao
import com.junjie.secdraservice.model.MessageSettings
import com.junjie.secdraservice.service.IMessageSettingsService
import javassist.NotFoundException
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.stereotype.Service

@Service
class MessageSettingsService(private val messageSettingsDao: IMessageSettingsDao) : IMessageSettingsService {
    @Cacheable("message::settings::get", key = "#userId")
    override fun get(userId: String): MessageSettings {
        val query = MessageSettings()
        query.userId = userId
        val matcher = ExampleMatcher.matching()
                .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("commentStatus")
                .withIgnorePaths("replyStatus")
                .withIgnorePaths("followStatus")
                .withIgnorePaths("systemStatus")
        val example = Example.of(query, matcher)
        return messageSettingsDao.findOne(example).orElseThrow { throw NotFoundException("找不到消息配置") }
    }

    @CachePut("message::settings::get", key = "#messageSettings.userId")
    override fun save(messageSettings: MessageSettings): MessageSettings {
        return messageSettingsDao.save(messageSettings)
    }
}