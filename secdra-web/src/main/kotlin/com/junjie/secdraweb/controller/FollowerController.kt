package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.service.IFollowService
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.vo.UserVo
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
class FollowerController(private val followService: IFollowService, private val userService: IUserService) {
    /**
     * 获取粉丝列表
     */
    @Auth
    @GetMapping("/paging")
    fun paging(@CurrentUserId followingId: String, id: String?, @PageableDefault(value = 20) pageable: Pageable): Page<UserVo> {
        val page = followService.pagingByFollowingId(
                if (id.isNullOrEmpty()) {
                    followingId
                } else {
                    id!!
                }, pageable)
        val userVoList = ArrayList<UserVo>()
        for (follow in page.content) {
            val userVo = UserVo(userService.getInfo(follow.followerId!!))
            userVo.isFocus = followService.exists(followingId, userVo.id!!)
            userVoList.add(userVo)
        }
        return PageImpl<UserVo>(userVoList, page.pageable, page.totalElements)
    }
}