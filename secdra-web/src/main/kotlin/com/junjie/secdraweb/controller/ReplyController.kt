package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdraservice.model.Reply
import com.junjie.secdraservice.model.ReplyMessage
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.ReplyMessageService
import com.junjie.secdraservice.service.ReplyService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.service.WebSocketService
import com.junjie.secdraweb.vo.ReplyVO
import com.junjie.secdraweb.vo.UserVO
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
                      private val userService: UserService,
                      private val replyMessageService: ReplyMessageService,
                      private val webSocketService: WebSocketService) {

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
        val vo = getVO(replyService.save(reply))
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
    fun list(commentId: String): List<ReplyVO> {
        return getListVO(replyService.list(commentId))
    }

    private fun getVO(reply: Reply): ReplyVO {
        val replyVO = ReplyVO(reply)
        replyVO.critic = UserVO(userService.getInfo(reply.criticId))
        replyVO.answerer = UserVO(userService.getInfo(replyVO.answererId))
        return replyVO
    }

    private fun getListVO(list: List<Reply>): List<ReplyVO> {
        return list.map { getVO(it) }
    }
}