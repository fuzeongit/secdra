package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.contant.NotifyType
import com.junjie.secdraservice.model.Comment
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Notify
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.ICommentService
import com.junjie.secdraservice.service.INotifyService
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
class CommentController(val commentService: ICommentService, val userService: IUserService,val notifyService: INotifyService) {

    /**
     * 发表评论
     */
    @Auth
    @PostMapping("/save")
    fun save(@CurrentUserId criticId: String, authorId: String, drawId: String, content: String): CommentVo {
        content.isEmpty() && throw Exception("评论不能为空")
        (authorId.isEmpty() || drawId.isEmpty()) && throw Exception("不能为空")
        val comment = Comment();
        comment.authorId = authorId;
        comment.criticId = criticId;
        comment.drawId = drawId;
        comment.content = content;

        val vo =  getVo(commentService.save(comment))
        val notify = Notify();
        notify.commentId = vo.id
        notify.receiveId = vo.authorId
        notify.authorId = vo.authorId
        notify.drawId = vo.drawId
        notify.criticId = vo.criticId
        notify.notifyType = NotifyType.COMMENT
        notify.content = vo.content
        notifyService.save(notify)
        return vo
    }

    /**
     * 获取4条
     */
    @GetMapping("listTop4")
    fun listTop4(drawId: String): List<CommentVo> {
//        val map = HashMap<String,Any>()
//        map["count"] = commentService.count(drawId)
//        map["list"] = getListVo(commentService.listTop4(drawId))
        return getListVo(commentService.listTop4(drawId))
    }

    /**
     * 获取列表
     */
    @GetMapping("list")
    fun list(drawId: String): List<CommentVo> {
        return getListVo(commentService.list(drawId))
    }

    /**
     * 分页获取
     */
    @GetMapping("pagingByDrawId")
    fun pagingByDrawId(drawId: String, @PageableDefault(value = 20) pageable: Pageable): Page<CommentVo> {
        return getPageVo(commentService.paging(drawId, pageable))
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

    private fun getListVo(list: List<Comment>): List<CommentVo> {
        val commentVoList = ArrayList<CommentVo>()
        for (comment in list) {
            commentVoList.add(getVo(comment))
        }
        return commentVoList
    }
}