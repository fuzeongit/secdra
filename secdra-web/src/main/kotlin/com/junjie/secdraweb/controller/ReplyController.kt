package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.model.Reply
import com.junjie.secdraservice.model.ReplyMessage
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.ReplyMessageService
import com.junjie.secdraservice.service.ReplyService
import com.junjie.secdraservice.service.UserService
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
class ReplyController(val replyService: ReplyService, val userService: UserService, val replyMessageService: ReplyMessageService) {

    /**
     * 发表评论
     */
    @Auth
    @PostMapping("/save")
    fun save(@CurrentUserId answererId: String, commentId: String, authorId: String, criticId: String, drawId: String, content: String): ReplyVO {
        content.isEmpty() && throw Exception("回复不能为空")
        (commentId.isEmpty() || authorId.isEmpty() || criticId.isEmpty() || drawId.isEmpty()) && throw Exception("不能为空")
        val reply = Reply()
        reply.answererId = answererId
        reply.commentId = commentId
        reply.authorId = authorId
        reply.criticId = criticId
        reply.drawId = drawId
        reply.content = content
        val vo =  getVO(replyService.save(reply))
        val replyMessage = ReplyMessage();
        replyMessage.commentId = vo.commentId
        replyMessage.replyId = vo.id
        replyMessage.authorId = vo.authorId
        replyMessage.drawId = vo.drawId
        replyMessage.criticId = vo.criticId
        replyMessage.answererId = vo.answererId
        replyMessage.content = vo.content
        replyMessageService.save(replyMessage)
        return vo
    }

    /**
     * 获取列表
     */
    @GetMapping("list")
    fun list(commentId: String): List<ReplyVO> {
        return getListVO(replyService.list(commentId))
    }

    private fun getVO(reply: Reply, critic: User? = null): ReplyVO {
        val replyVO = ReplyVO(reply)
        if (critic == null) {
            replyVO.critic = UserVO(userService.getInfo(reply.criticId!!))
        } else {
            replyVO.critic = UserVO(critic)
        }
        replyVO.answerer = UserVO(userService.getInfo(replyVO.answererId!!))
        return replyVO
    }

    private fun getListVO(list: List<Reply>): List<ReplyVO> {
        val replyVOList = ArrayList<ReplyVO>()
        for (reply in list) {
            replyVOList.add(getVO(reply))
        }
        return replyVOList
    }
}