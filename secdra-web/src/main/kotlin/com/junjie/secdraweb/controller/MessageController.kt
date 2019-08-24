package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdraservice.constant.MessageType
import com.junjie.secdraservice.model.CommentMessage
import com.junjie.secdraservice.model.FollowMessage
import com.junjie.secdraservice.model.MessageSettings
import com.junjie.secdraservice.model.ReplyMessage
import com.junjie.secdraservice.service.*
import com.junjie.secdraweb.base.communal.UserVOAbstract
import com.junjie.secdraweb.vo.CommentMessageVO
import com.junjie.secdraweb.vo.FollowMessageVO
import com.junjie.secdraweb.vo.ReplyMessageVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("message")
class MessageController(private val commentMessageService: CommentMessageService,
                        private val replyMessageService: ReplyMessageService,
                        private val followMessageService: FollowMessageService,
                        private val systemMessageService: SystemMessageService,
                        private val messageSettingsService: MessageSettingsService,
                        override val userService: UserService,
                        override val followService: FollowService) : UserVOAbstract() {
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
                item.review && continue
                item.review = true
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

    private fun getCommentMessageListVO(list: List<CommentMessage>): List<CommentMessageVO> {
        val voList = mutableListOf<CommentMessageVO>()
        for (commentMessage in list) {
            voList.add(CommentMessageVO(commentMessage, getUserVO(commentMessage.criticId, commentMessage.authorId)))
            commentMessage.review && continue
            commentMessage.review = true
            commentMessageService.save(commentMessage)
        }
        return voList
    }

    private fun getReplyMessageListVO(list: List<ReplyMessage>): List<ReplyMessageVO> {
        val voList = mutableListOf<ReplyMessageVO>()
        for (replyMessage in list) {
            voList.add(ReplyMessageVO(replyMessage, getUserVO(userService.getInfo(replyMessage.answererId), replyMessage.criticId)))
            replyMessage.review && continue
            replyMessage.review = true
            replyMessageService.save(replyMessage)
        }
        return voList
    }

    private fun getFollowMessageListVO(list: List<FollowMessage>): List<FollowMessageVO> {
        val voList = mutableListOf<FollowMessageVO>()
        for (followMessage in list) {
            voList.add(FollowMessageVO(followMessage, getUserVO(userService.getInfo(followMessage.followerId), followMessage.followingId)))
            followMessage.review && continue
            followMessage.review = true
            followMessageService.save(followMessage)
        }
        return voList
    }
}
