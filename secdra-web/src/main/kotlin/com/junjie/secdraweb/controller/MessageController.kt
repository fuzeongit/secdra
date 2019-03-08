package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.constant.MessageType
import com.junjie.secdraservice.model.*
import com.junjie.secdraservice.service.*
import com.junjie.secdraweb.vo.CommentMessageVo
import com.junjie.secdraweb.vo.FollowMessageVo
import com.junjie.secdraweb.vo.ReplyMessageVo
import com.junjie.secdraweb.vo.UserVo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("message")
class MessageController(private val userService: IUserService, private val commentMessageService: ICommentMessageService,
                        private val replyMessageService: IReplyMessageService, private val followMessageService: IFollowMessageService,
                        private val systemMessageService: ISystemMessageService, private val messageSettingsService: IMessageSettingsService) {
    @Auth
    @GetMapping("/count")
    fun count(@CurrentUserId userId: String, messageType: MessageType?): HashMap<MessageType, Long> {
        val vo = HashMap<MessageType, Long>()
        val messageSettings = try {
            messageSettingsService.get(userId)
        } catch (e: Exception) {
            val newSettings = MessageSettings()
            newSettings.userId = userId
            messageSettingsService.save(newSettings)
        }
        vo[MessageType.COMMENT] = if (messageSettings.commentStatus) {
            commentMessageService.countUnread(userId)
        } else 0
        vo[MessageType.REPLY] = if (messageSettings.replyStatus) {
            replyMessageService.countUnread(userId)
        } else 0
        vo[MessageType.FOLLOW] = if (messageSettings.followStatus) {
            followMessageService.countUnread(userId)
        } else 0
        vo[MessageType.SYSTEM] = systemMessageService.countUnread(userId)
        return vo
    }

    @Auth
    @GetMapping("/listUnread")
    fun listUnread(@CurrentUserId userId: String, messageType: MessageType): List<Any> {
        if (messageType == MessageType.COMMENT) {
            return getCommentMessageListVo(commentMessageService.listUnread(userId))
        }
        if (messageType == MessageType.REPLY) {
            return getReplyMessageListVo(replyMessageService.listUnread(userId))
        }
        if (messageType == MessageType.FOLLOW) {
            return getFollowMessageListVo(followMessageService.listUnread(userId))
        }
        if (messageType == MessageType.SYSTEM) {
            return systemMessageService.listUnread(userId)
        }
        return listOf()
    }

    @Auth
    @GetMapping("/list")
    fun list(@CurrentUserId userId: String, messageType: MessageType): List<Any> {
        if (messageType == MessageType.COMMENT) {
            return getCommentMessageListVo(commentMessageService.list(userId))
        }
        if (messageType == MessageType.REPLY) {
            return getReplyMessageListVo(replyMessageService.list(userId))
        }
        if (messageType == MessageType.FOLLOW) {
            return getFollowMessageListVo(followMessageService.list(userId))
        }
        if (messageType == MessageType.SYSTEM) {
            val list = systemMessageService.list(userId)
            for (item in list) {
                if (item.isRead) {
                    continue
                }
                item.isRead = true;
                systemMessageService.save(item)
            }
            return list
        }
        return listOf()
    }

    @Auth
    @GetMapping("/getSettings")
    fun getSettings(@CurrentUserId userId: String): MessageSettings {
        return try {
            messageSettingsService.get(userId)
        } catch (e: Exception) {
            val newSettings = MessageSettings()
            newSettings.userId = userId
            messageSettingsService.save(newSettings)
        }
    }

    @Auth
    @PostMapping("/saveSettings")
    fun saveSettings(@CurrentUserId userId: String, id: String, commentStatus: Boolean, replyStatus: Boolean, followStatus: Boolean): MessageSettings {
        val messageSettings = MessageSettings()
        messageSettings.id = id
        messageSettings.userId = userId
        messageSettings.commentStatus = commentStatus
        messageSettings.replyStatus = replyStatus
        messageSettings.followStatus = followStatus
        return messageSettingsService.save(messageSettings)
    }

    private fun getCommentMessageVo(commentMessage: CommentMessage): CommentMessageVo {
        val vo = CommentMessageVo(commentMessage)
        vo.critic = UserVo(userService.getInfo(vo.criticId!!))
        return vo
    }

    private fun getCommentMessageListVo(list: List<CommentMessage>): List<CommentMessageVo> {
        val voList = mutableListOf<CommentMessageVo>()
        for (commentMessage in list) {
            voList.add(getCommentMessageVo(commentMessage))
            if (commentMessage.isRead) {
                continue
            }
            commentMessage.isRead = true;
            commentMessageService.save(commentMessage)
        }
        return voList
    }

    private fun getReplyMessageVo(replyMessage: ReplyMessage): ReplyMessageVo {
        val vo = ReplyMessageVo(replyMessage)
        vo.answerer = UserVo(userService.getInfo(vo.answererId!!))
        return vo
    }

    private fun getReplyMessageListVo(list: List<ReplyMessage>): List<ReplyMessageVo> {
        val voList = mutableListOf<ReplyMessageVo>()
        for (replyMessage in list) {
            voList.add(getReplyMessageVo(replyMessage))
            if (replyMessage.isRead) {
                continue
            }
            replyMessage.isRead = true;
            replyMessageService.save(replyMessage)
        }
        return voList
    }

    private fun getFollowMessageVo(followMessage: FollowMessage): FollowMessageVo {
        val vo = FollowMessageVo(followMessage)
        vo.follower = UserVo(userService.getInfo(vo.followerId!!))
        return vo
    }

    private fun getFollowMessageListVo(list: List<FollowMessage>): List<FollowMessageVo> {
        val voList = mutableListOf<FollowMessageVo>()
        for (followMessage in list) {
            voList.add(getFollowMessageVo(followMessage))
            if (followMessage.isRead) {
                continue
            }
            followMessage.isRead = true;
            followMessageService.save(followMessage)
        }
        return voList
    }
}
