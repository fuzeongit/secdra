package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdraservice.constant.MessageType
import com.junjie.secdraservice.model.*
import com.junjie.secdraservice.service.*
import com.junjie.secdraweb.vo.CommentMessageVO
import com.junjie.secdraweb.vo.FollowMessageVO
import com.junjie.secdraweb.vo.ReplyMessageVO
import com.junjie.secdraweb.vo.UserVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("message")
class MessageController(private val userService: UserService, private val commentMessageService: CommentMessageService,
                        private val replyMessageService: ReplyMessageService, private val followMessageService: FollowMessageService,
                        private val systemMessageService: SystemMessageService, private val messageSettingsService: MessageSettingsService) {
    @Auth
    @GetMapping("/count")
    @RestfulPack
    fun count(@CurrentUserId userId: String, messageType: MessageType?): HashMap<MessageType, Long> {
        val vo = HashMap<MessageType, Long>()
        vo[MessageType.COMMENT] = commentMessageService.countUnread(userId)
        vo[MessageType.REPLY] = replyMessageService.countUnread(userId)
        vo[MessageType.FOLLOW] = followMessageService.countUnread(userId)
        vo[MessageType.SYSTEM] = systemMessageService.countUnread(userId)
        return vo
    }

    @Auth
    @GetMapping("/listUnread")
    @RestfulPack
    fun listUnread(@CurrentUserId userId: String, messageType: MessageType): List<Any> {
        if (messageType == MessageType.COMMENT) {
            return getCommentMessageListVO(commentMessageService.listUnread(userId))
        }
        if (messageType == MessageType.REPLY) {
            return getReplyMessageListVO(replyMessageService.listUnread(userId))
        }
        if (messageType == MessageType.FOLLOW) {
            return getFollowMessageListVO(followMessageService.listUnread(userId))
        }
        if (messageType == MessageType.SYSTEM) {
            return systemMessageService.listUnread(userId)
        }
        return listOf()
    }

    @Auth
    @GetMapping("/list")
    @RestfulPack
    fun list(@CurrentUserId userId: String, messageType: MessageType): List<Any> {
        if (messageType == MessageType.COMMENT) {
            return getCommentMessageListVO(commentMessageService.list(userId))
        }
        if (messageType == MessageType.REPLY) {
            return getReplyMessageListVO(replyMessageService.list(userId))
        }
        if (messageType == MessageType.FOLLOW) {
            return getFollowMessageListVO(followMessageService.list(userId))
        }
        if (messageType == MessageType.SYSTEM) {
            val list = systemMessageService.list(userId)
            for (item in list) {
                if (item.isRead) {
                    continue
                }
                item.isRead = true
                systemMessageService.save(item)
            }
            return list
        }
        return listOf()
    }

    @Auth
    @GetMapping("/getSettings")
    @RestfulPack
    fun getSettings(@CurrentUserId userId: String): MessageSettings {
        return try {
            messageSettingsService.get(userId)
        } catch (e: Exception) {
            val newSettings = MessageSettings(userId)
            messageSettingsService.save(newSettings)
        }
    }

    @Auth
    @PostMapping("/saveSettings")
    @RestfulPack
    fun saveSettings(@CurrentUserId userId: String, id: String, commentStatus: Boolean, replyStatus: Boolean, followStatus: Boolean): MessageSettings {
        val messageSettings = MessageSettings(userId)
        messageSettings.id = id
        messageSettings.commentStatus = commentStatus
        messageSettings.replyStatus = replyStatus
        messageSettings.followStatus = followStatus
        return messageSettingsService.save(messageSettings)
    }

    private fun getCommentMessageVO(commentMessage: CommentMessage): CommentMessageVO {
        val vo = CommentMessageVO(commentMessage)
        vo.critic = UserVO(userService.getInfo(vo.criticId))
        return vo
    }

    private fun getCommentMessageListVO(list: List<CommentMessage>): List<CommentMessageVO> {
        val voList = mutableListOf<CommentMessageVO>()
        for (commentMessage in list) {
            voList.add(getCommentMessageVO(commentMessage))
            if (commentMessage.isRead) {
                continue
            }
            commentMessage.isRead = true
            commentMessageService.save(commentMessage)
        }
        return voList
    }

    private fun getReplyMessageVO(replyMessage: ReplyMessage): ReplyMessageVO {
        val vo = ReplyMessageVO(replyMessage)
        vo.answerer = UserVO(userService.getInfo(vo.answererId))
        return vo
    }

    private fun getReplyMessageListVO(list: List<ReplyMessage>): List<ReplyMessageVO> {
        val voList = mutableListOf<ReplyMessageVO>()
        for (replyMessage in list) {
            voList.add(getReplyMessageVO(replyMessage))
            if (replyMessage.isRead) {
                continue
            }
            replyMessage.isRead = true
            replyMessageService.save(replyMessage)
        }
        return voList
    }

    private fun getFollowMessageVO(followMessage: FollowMessage): FollowMessageVO {
        val vo = FollowMessageVO(followMessage)
        vo.follower = UserVO(userService.getInfo(vo.followerId))
        return vo
    }

    private fun getFollowMessageListVO(list: List<FollowMessage>): List<FollowMessageVO> {
        val voList = mutableListOf<FollowMessageVO>()
        for (followMessage in list) {
            voList.add(getFollowMessageVO(followMessage))
            if (followMessage.isRead) {
                continue
            }
            followMessage.isRead = true
            followMessageService.save(followMessage)
        }
        return voList
    }
}
