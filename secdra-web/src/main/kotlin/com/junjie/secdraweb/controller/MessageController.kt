package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.contant.MessageType
import com.junjie.secdraservice.model.CommentMessage
import com.junjie.secdraservice.model.ReplyMessage
import com.junjie.secdraservice.service.ICommentMessageService
import com.junjie.secdraservice.service.IReplyMessageService
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.vo.CommentMessageVo
import com.junjie.secdraweb.vo.ReplyMessageVo
import com.junjie.secdraweb.vo.UserVo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("message")
class MessageController(private val userService: IUserService, private val commentMessageService: ICommentMessageService, private val replyMessageService: IReplyMessageService) {
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
//        val userInfo = userService.getInfo(userId)
        if (messageType == MessageType.COMMENT) {
            return getCommentMessageListVo(commentMessageService.listUnread(userId))
        }
        if (messageType == MessageType.REPLY) {
            return getReplyMessageListVo(replyMessageService.listUnread(userId))
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
        return listOf()
    }

    private fun getCommentMessageVo(commentMessage :CommentMessage):CommentMessageVo{
        val vo = CommentMessageVo(commentMessage)
        vo.critic = UserVo(userService.getInfo(vo.criticId!!))
        return vo
    }
    private fun getCommentMessageListVo(list :List<CommentMessage>):List<CommentMessageVo>{
        val voList = mutableListOf<CommentMessageVo>()
       for(commentMessage in list){
           voList.add(getCommentMessageVo(commentMessage))
       }
        return voList
    }

    private fun getReplyMessageVo(replyMessage : ReplyMessage): ReplyMessageVo {
        val vo = ReplyMessageVo(replyMessage)
        vo.answerer = UserVo(userService.getInfo(vo.answererId!!))
        return vo
    }
    private fun getReplyMessageListVo(list :List<ReplyMessage>):List<ReplyMessageVo>{
        val voList = mutableListOf<ReplyMessageVo>()
        for(replyMessage in list){
            voList.add(getReplyMessageVo(replyMessage))
        }
        return voList
    }
}
