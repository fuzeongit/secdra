package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.model.Comment
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Reply
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.ICommentService
import com.junjie.secdraservice.service.IReplyService
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.vo.CommentVo
import com.junjie.secdraweb.vo.DrawVo
import com.junjie.secdraweb.vo.ReplyVo
import com.junjie.secdraweb.vo.UserVo
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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
class ReplyController(val replyService: IReplyService, val userService: IUserService) {

    /**
     * 发表评论
     */
    @Auth
    @PostMapping("/save")
    fun save(@CurrentUserId answererId: String, commentId: String, authorId: String, criticId: String, drawId: String, content: String): ReplyVo {
        val reply = Reply()
        reply.answererId = answererId
        reply.commentId = commentId
        reply.authorId = authorId
        reply.criticId = criticId
        reply.drawId = drawId
        reply.content = content
        return getVo(replyService.save(reply))
    }

    /**
     * 获取未读
     */
    @Auth
    @GetMapping("/listUnread")
    fun listUnread(@CurrentUserId userId: String): List<ReplyVo> {
        val replyList = replyService.listUnread(userId)
        val replyVoList = mutableListOf<ReplyVo>()
        val user = userService.getInfo(userId)
        for (reply in replyList) {
            replyVoList.add(getVo(reply, user))
        }
        return replyVoList
    }

    /**
     * 获取未读数量
     */
    @Auth
    @GetMapping("/countUnread")
    fun countUnread(@CurrentUserId userId: String): Long {
        return replyService.countUnread(userId)
    }

    /**
     * 获取列表
     */
    @GetMapping("list")
    fun list(commentId: String): List<ReplyVo> {
        return getListVo(replyService.list(commentId))
    }

    private fun getVo(reply: Reply, critic: User? = null, authorId: User? = null): ReplyVo {
        val replyVo = ReplyVo(reply)
        if (critic == null) {
            replyVo.critic = UserVo(userService.getInfo(reply.criticId!!))
        } else {
            replyVo.critic = UserVo(critic)
        }
        replyVo.answerer = UserVo(userService.getInfo(replyVo.answererId!!))
        return replyVo
    }

    private fun getListVo(list: List<Reply>): List<ReplyVo> {
        val replyVoList = ArrayList<ReplyVo>()
        for (reply in list) {
            replyVoList.add(getVo(reply))
        }
        return replyVoList
    }
}