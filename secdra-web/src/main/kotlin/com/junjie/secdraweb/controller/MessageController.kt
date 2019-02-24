package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.contant.MessageType
import com.junjie.secdraservice.service.ICommentMessageService
import com.junjie.secdraservice.service.IReplyMessageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("message")
class MessageController(private val commentMessageService: ICommentMessageService, private val replyMessageService: IReplyMessageService) {
    @Auth
    @GetMapping("/count")
    fun count(@CurrentUserId userId: String, messageType: MessageType?): Long {
        var count: Long = 0
        if (messageType == null || messageType == MessageType.COMMENT) {
            count += commentMessageService.countUnread(userId)
        }
        if (messageType == null || messageType == MessageType.REPLY) {
            count += replyMessageService.countUnread(userId)
        }
        return count
    }

    @Auth
    @GetMapping("/listUnread")
    fun listUnread(@CurrentUserId userId: String, messageType: MessageType): List<Any> {
        if (messageType == MessageType.COMMENT) {
            return commentMessageService.listUnread(userId)
        }
        if (messageType == MessageType.REPLY) {
            return replyMessageService.listUnread(userId)
        }
        return listOf()
    }

    @Auth
    @GetMapping("/list")
    fun list(@CurrentUserId userId: String, messageType: MessageType): List<Any> {

        if (messageType == MessageType.COMMENT) {
            return commentMessageService.list(userId)
        }
        if (messageType == MessageType.REPLY) {
            return replyMessageService.list(userId)
        }
        return listOf()
    }
}
