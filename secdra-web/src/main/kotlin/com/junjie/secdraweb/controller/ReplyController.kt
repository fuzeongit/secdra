package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdraservice.model.Reply
import com.junjie.secdraservice.model.ReplyMessage
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.ReplyMessageService
import com.junjie.secdraservice.service.ReplyService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.base.communal.UserVOAbstract
import com.junjie.secdraweb.service.WebSocketService
import com.junjie.secdraweb.vo.ReplyVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author fjj
 * 回复的控制器
 */
@RestController
@RequestMapping("reply")
class ReplyController(private val replyService: ReplyService,
                      private val replyMessageService: ReplyMessageService,
                      private val webSocketService: WebSocketService,
                      override val userService: UserService,
                      override val followService: FollowService) : UserVOAbstract() {

    /**
     * 发表评论
     */
    @Auth
    @PostMapping("/save")
    @RestfulPack
    fun save(@CurrentUserId answererId: String, commentId: String, authorId: String, criticId: String, drawId: String, content: String): ReplyVO {
        content.isEmpty() && throw Exception("回复不能为空")
        (commentId.isEmpty() || authorId.isEmpty() || criticId.isEmpty() || drawId.isEmpty()) && throw Exception("不能为空")
        val reply = Reply(commentId, authorId, criticId, answererId, drawId, content)
        val vo = ReplyVO(replyService.save(reply), getUserVO(criticId, answererId), getUserVO(answererId, answererId))
        val replyMessage = ReplyMessage(vo.commentId, vo.id, vo.authorId, vo.drawId, vo.criticId, vo.answererId, vo.content)
        replyMessageService.save(replyMessage)
        webSocketService.sendReply(answererId, criticId)
        return vo
    }

    /**
     * 获取列表
     */
    @GetMapping("list")
    @RestfulPack
    fun list(@CurrentUserId userId: String, commentId: String): List<ReplyVO> {
        return replyService.list(commentId).map {
            ReplyVO(it, getUserVO(it.criticId, userId), getUserVO(it.answererId, userId))
        }
    }
}