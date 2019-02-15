package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.model.Comment
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.ICommentService
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.vo.CommentVo
import com.junjie.secdraweb.vo.DrawVo
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
 * 评论的控制器
 */
@RestController
@RequestMapping("comment")
class CommentController(val commentService: ICommentService, val userService: IUserService) {

    /**
     * 发表评论
     */
    @Auth
    @PostMapping("/save")
    fun save(@CurrentUserId criticId: String, authorId: String, drawId: String, content: String): CommentVo {
        val comment = Comment();
        comment.authorId = authorId;
        comment.criticId = criticId;
        comment.drawId = drawId;
        comment.content = content;
        return getVo(comment)
    }

    /**
     * 获取未读
     */
    @Auth
    @GetMapping("/listUnread")
    fun listUnread(@CurrentUserId userId: String): List<CommentVo> {
        val commentList = commentService.listUnread(userId)
        val commentVoList = mutableListOf<CommentVo>()
        val user = userService.getInfo(userId)
        for (comment in commentList) {
            commentVoList.add(getVo(comment, user))
        }
        return commentVoList
    }

    /**
     * 获取未读数量
     */
    @Auth
    @GetMapping("/countUnread")
    fun countUnread(@CurrentUserId userId: String): Long {
        return commentService.countUnread(userId)
    }

    /**
     * 分页获取
     */
    @GetMapping("pagingByDrawId")
    fun pagingByDrawId(drawId: String, @PageableDefault(value = 20) pageable: Pageable): Page<CommentVo> {
        return getPageVo(commentService.pagingByDrawId(drawId, pageable))
    }

    private fun getVo(comment: Comment, user: User? = null): CommentVo {
        val commentVo = CommentVo(comment)
        if (user == null) {
            commentVo.author = UserVo(userService.getInfo(comment.authorId!!))
        } else {
            commentVo.author = UserVo(user)
        }
        commentVo.critic = UserVo(userService.getInfo(comment.criticId!!))
        return commentVo
    }

    private fun getPageVo(page: Page<Comment>): Page<CommentVo> {
        val commentVoList = ArrayList<CommentVo>()
        for (comment in page.content) {
            commentVoList.add(getVo(comment))
        }
        return PageImpl<CommentVo>(commentVoList, page.pageable, page.totalElements)
    }
}