package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.SignInException
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.core.communal.UserVOAbstract
import com.junjie.secdraweb.vo.UserVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author fjj
 * 粉丝的控制器
 */
@RestController
@RequestMapping("follower")
class FollowerController(override val userService: UserService, override val followService: FollowService) : UserVOAbstract() {
    /**
     * 获取粉丝列表
     */
    @GetMapping("paging")
    @RestfulPack
    fun paging(@CurrentUserId followingId: String?, id: String?, @PageableDefault(value = 20) pageable: Pageable): Page<UserVO> {
        if (followingId.isNullOrEmpty() && id.isNullOrEmpty()) throw SignInException("请重新登录")
        val page = followService.pagingByFollowingId(id ?: followingId!!, pageable)
        val userVOList = page.content.map {
            getUserVO(it.createdBy!!, followingId)
        }
        return PageImpl(userVOList, page.pageable, page.totalElements)
    }
}