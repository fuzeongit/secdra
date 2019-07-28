package com.junjie.secdraweb.service

import com.junjie.secdracore.model.Result
import com.junjie.secdraservice.service.FollowMessageService
import com.junjie.secdraservice.service.MessageSettingsService
import com.junjie.secdraservice.service.UserService
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class WebSocketService(private val simpMessagingTemplate: SimpMessagingTemplate, private val userService: UserService,
                       private val followMessageService: FollowMessageService,
                       private val messageSettingsService: MessageSettingsService) {
    @Async
    fun sendFollowingFocus(followerId: String, followingId: String) {
        val messageSettings = messageSettingsService.get(followingId)
        val countUnread = followMessageService.countUnread(followingId)
        if (messageSettings.followStatus) {
            simpMessagingTemplate.convertAndSendToUser(followingId, "/following/focus", Result<Any>(200, userService.getInfo(followerId).name + "关注了你", countUnread))
        } else {
            simpMessagingTemplate.convertAndSendToUser(followingId, "/following/focus", Result<Any>(200, null, countUnread))
        }
    }
}