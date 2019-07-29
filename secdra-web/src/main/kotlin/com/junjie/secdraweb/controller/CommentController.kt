package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.model.Comment
import com.junjie.secdraservice.model.CommentMessage
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.CommentMessageService
import com.junjie.secdraservice.service.CommentService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.service.WebSocketService
import com.junjie.secdraweb.vo.CommentVO
import com.junjie.secdraweb.vo.UserVO
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
                        private val userService: UserService,
                        private val commentMessageService: CommentMessageService,
                        private val webSocketService: WebSocketService) {

    /**
     * 发表评论
     */
    @Auth
    @PostMapping("/save")
    fun save(@CurrentUserId criticId: String, authorId: String, drawId: String, content: String): CommentVO {
        content.isEmpty() && throw Exception("评论不能为空")
        (authorId.isEmpty() || drawId.isEmpty()) && throw Exception("不能为空")
        val comment = Comment();
        comment.authorId = authorId;
        comment.criticId = criticId;
        comment.drawId = drawId;
        comment.content = content;

        val vo = getVO(commentService.save(comment))
        val commentMessage = CommentMessage();
        commentMessage.commentId = vo.id
        commentMessage.authorId = vo.authorId
        commentMessage.drawId = vo.drawId
        commentMessage.criticId = vo.criticId
        commentMessage.content = vo.content
        commentMessageService.save(commentMessage)
        webSocketService.sendComment(criticId, authorId)
        return vo
    }

    /**
     * 获取4条
     */
    @GetMapping("listTop4")
    fun listTop4(drawId: String): List<CommentVO> {
//        val map = HashMap<String,Any>()
//        map["count"] = commentService.count(drawId)
//        map["list"] = getListVO(commentService.listTop4(drawId))
        return getListVO(commentService.listTop4(drawId))
    }

    /**
     * 获取列表
     */
    @GetMapping("list")
    fun list(drawId: String): List<CommentVO> {
        return getListVO(commentService.list(drawId))
    }

    /**
     * 分页获取
     */
    @GetMapping("pagingByDrawId")
    fun pagingByDrawId(drawId: String, @PageableDefault(value = 20) pageable: Pageable): Page<CommentVO> {
        return getPageVO(commentService.paging(drawId, pageable))
    }

    private fun getVO(comment: Comment, user: User? = null): CommentVO {
        val commentVO = CommentVO(comment)
        if (user == null) {
            commentVO.author = UserVO(userService.getInfo(comment.authorId!!))
        } else {
            commentVO.author = UserVO(user)
        }
        commentVO.critic = UserVO(userService.getInfo(comment.criticId!!))
        return commentVO
    }

    private fun getPageVO(page: Page<Comment>): Page<CommentVO> {
        val commentVOList = ArrayList<CommentVO>()
        for (comment in page.content) {
            commentVOList.add(getVO(comment))
        }
        return PageImpl<CommentVO>(commentVOList, page.pageable, page.totalElements)
    }

    private fun getListVO(list: List<Comment>): List<CommentVO> {
        val commentVOList = ArrayList<CommentVO>()
        for (comment in list) {
            commentVOList.add(getVO(comment))
        }
        return commentVOList
    }
}