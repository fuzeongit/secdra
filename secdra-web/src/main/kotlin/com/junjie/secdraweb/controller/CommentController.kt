package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdradata.database.primary.entity.Comment
import com.junjie.secdradata.database.primary.entity.CommentMessage
import com.junjie.secdraservice.service.CommentMessageService
import com.junjie.secdraservice.service.CommentService
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.core.communal.UserVOAbstract
import com.junjie.secdraweb.service.WebSocketService
import com.junjie.secdraweb.vo.CommentVO
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
class CommentController(private val commentService: CommentService,
                        private val commentMessageService: CommentMessageService,
                        private val webSocketService: WebSocketService,
                        override val userService: UserService,
                        override val followService: FollowService) : UserVOAbstract() {

    /**
     * 发表评论
     */
    @Auth
    @PostMapping("save")
    @RestfulPack
    fun save(@CurrentUserId criticId: String, authorId: String, pictureId: String, content: String): CommentVO {
        content.isEmpty() && throw Exception("评论不能为空")
        (authorId.isEmpty() || pictureId.isEmpty()) && throw Exception("不能为空")
        val comment = Comment(authorId, criticId, pictureId, content)
        val vo = CommentVO(
                commentService.save(comment),
                getUserVO(authorId, criticId),
                getUserVO(criticId, criticId)
        )
        val commentMessage = CommentMessage(vo.id, vo.authorId, vo.pictureId, vo.criticId, vo.content)
        commentMessageService.save(commentMessage)
        webSocketService.sendComment(criticId, authorId)
        return vo
    }

    /**
     * 获取4条
     */
    @GetMapping("listTop4")
    @RestfulPack
    fun listTop4(@CurrentUserId userId: String?, pictureId: String): List<CommentVO> {
        return getListVO(commentService.listTop4(pictureId), userId)
    }

    /**
     * 获取列表
     */
    @GetMapping("list")
    @RestfulPack
    fun list(@CurrentUserId userId: String?, pictureId: String): List<CommentVO> {
        return getListVO(commentService.list(pictureId), userId)
    }

    /**
     * 分页获取
     */
    @GetMapping("pagingByPictureId")
    @RestfulPack
    fun pagingByPictureId(@CurrentUserId userId: String?, pictureId: String, @PageableDefault(value = 20) pageable: Pageable): Page<CommentVO> {
        return getPageVO(commentService.paging(pictureId, pageable), userId)
    }

    private fun getPageVO(page: Page<Comment>, userId: String? = null): Page<CommentVO> {
        val commentVOList = getListVO(page.content, userId)
        return PageImpl(commentVOList, page.pageable, page.totalElements)
    }

    private fun getListVO(list: List<Comment>, userId: String? = null): List<CommentVO> {
        return list.map {
            CommentVO(
                    it,
                    getUserVO(it.authorId, userId),
                    getUserVO(it.criticId, userId)
            )
        }
    }
}