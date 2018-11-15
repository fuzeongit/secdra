package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.model.Comment
import com.junjie.secdraservice.service.ICommentService
import org.springframework.data.domain.Page
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
class CommentController(val commentService: ICommentService) {

    /**
     * 发表
     */
    @Auth
    @PostMapping("/save")
    fun save(@CurrentUserId userId: String, drawId: String, content: String): Comment {
        return commentService.save(userId, drawId, content)
    }

    /**
     * 获取未读
     */
    @Auth
    @GetMapping("/pagingUnread")
    fun pagingUnread(@CurrentUserId userId: String, @PageableDefault(value = 20) pageable: Pageable): Page<Comment>{
        return commentService.pagingUnread(userId, pageable)
    }

    /**
     * 分页获取
     */
    @GetMapping("pagingByDrawId")
    fun pagingByDrawId(drawId: String,@PageableDefault(value = 20) pageable: Pageable): Page<Comment>{
        return commentService.pagingByDrawId(drawId, pageable)
    }

}