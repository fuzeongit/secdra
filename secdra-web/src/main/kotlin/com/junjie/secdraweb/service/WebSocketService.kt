package com.junjie.secdraweb.service

import com.junjie.secdracore.model.Result
import com.junjie.secdraservice.service.*
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class WebSocketService(private val simpMessagingTemplate: SimpMessagingTemplate, private val userService: UserService,
                       private val commentMessageService: CommentMessageService,
                       private val followMessageService: FollowMessageService,
                       private val messageSettingsService: MessageSettingsService,
                       private val replyMessageService: ReplyMessageService) {
    @Async
    fun sendComment(criticId: String, authorId: String) {
        if (criticId == authorId) return
        val messageSettings = messageSettingsService.get(authorId)
        val countUnread = commentMessageService.countUnread(authorId)
        if (messageSettings.followStatus) {
            simpMessagingTemplate.convertAndSendToUser(authorId, "/comment/send", Result<Any>(200, userService.get(criticId).name + "评论了你的作品", countUnread))
        } else {
            simpMessagingTemplate.convertAndSendToUser(authorId, "/comment/send", Result<Any>(200, null, countUnread))
        }
    }

    @Async
    fun sendReply(answererId: String, criticId: String) {
        if (answererId == criticId) return
        val messageSettings = messageSettingsService.get(criticId)
        val countUnread = replyMessageService.countUnread(criticId)
        if (messageSettings.followStatus) {
            simpMessagingTemplate.convertAndSendToUser(criticId, "/reply/send", Result<Any>(200, userService.get(answererId).name + "回复了你的评论", countUnread))
        } else {
            simpMessagingTemplate.convertAndSendToUser(criticId, "/reply/send", Result<Any>(200, null, countUnread))
        }
    }

    @Async
    fun sendFollowingFocus(followerId: String, followingId: String) {
        val messageSettings = messageSettingsService.get(followingId)
        val countUnread = followMessageService.countUnread(followingId)
        if (messageSettings.followStatus) {
            simpMessagingTemplate.convertAndSendToUser(followingId, "/following/focus", Result<Any>(200, userService.get(followerId).name + "关注了你", countUnread))
        } else {
            simpMessagingTemplate.convertAndSendToUser(followingId, "/following/focus", Result<Any>(200, null, countUnread))
        }
    }
}