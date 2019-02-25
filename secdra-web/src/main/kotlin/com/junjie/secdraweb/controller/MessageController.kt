package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.contant.MessageType
import com.junjie.secdraservice.model.CommentMessage
import com.junjie.secdraservice.model.FollowMessage
import com.junjie.secdraservice.model.ReplyMessage
import com.junjie.secdraservice.service.ICommentMessageService
import com.junjie.secdraservice.service.IFollowMessageService
import com.junjie.secdraservice.service.IReplyMessageService
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.vo.CommentMessageVo
import com.junjie.secdraweb.vo.FollowMessageVo
import com.junjie.secdraweb.vo.ReplyMessageVo
import com.junjie.secdraweb.vo.UserVo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("message")
class MessageController(private val userService: IUserService, private val commentMessageService: ICommentMessageService,
                        private val replyMessageService: IReplyMessageService,private val followMessageService: IFollowMessageService) {
    @Auth
    @GetMapping("/count")
    fun count(@CurrentUserId userId: String, messageType: MessageType?): HashMap<MessageType, Long> {
        val vo = HashMap<MessageType,Long>()
        vo[MessageType.COMMENT] = commentMessageService.countUnread(userId)
        vo[MessageType.REPLY] = replyMessageService.countUnread(userId)
        vo[MessageType.FOLLOW] = followMessageService.countUnread(userId)
//        var count: Long = 0
//        if (messageType == null || messageType == MessageType.COMMENT) {
//            count += commentMessageService.countUnread(userId)
//        }
//        if (messageType == null || messageType == MessageType.REPLY) {
//            count += replyMessageService.countUnread(userId)
//        }
//        if (messageType == null || messageType == MessageType.FOLLOW) {
//            count += followMessageService.countUnread(userId)
//        }
        return vo
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
        if (messageType == MessageType.FOLLOW) {
            return getFollowMessageListVo(followMessageService.listUnread(userId))
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

    private fun getFollowMessageVo(followMessage : FollowMessage): FollowMessageVo {
        val vo = FollowMessageVo(followMessage)
        vo.follower = UserVo(userService.getInfo(vo.followerId!!))
        return vo
    }
    private fun getFollowMessageListVo(list :List<FollowMessage>):List<FollowMessageVo>{
        val voList = mutableListOf<FollowMessageVo>()
        for(followMessage in list){
            voList.add(getFollowMessageVo(followMessage))
        }
        return voList
    }
}
