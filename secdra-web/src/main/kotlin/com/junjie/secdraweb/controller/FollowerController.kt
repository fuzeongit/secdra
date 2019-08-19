package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
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
class FollowerController(private val followService: FollowService, private val userService: UserService) {
    /**
     * 获取粉丝列表
     */
    @Auth
    @GetMapping("/paging")
    @RestfulPack
    fun paging(@CurrentUserId followingId: String, id: String?, @PageableDefault(value = 20) pageable: Pageable): Page<UserVO> {
        val page = followService.pagingByFollowingId(
                if (id.isNullOrEmpty()) {
                    followingId
                } else {
                    id!!
                }, pageable)
        val userVOList = ArrayList<UserVO>()
        for (follow in page.content) {
            val userVO = UserVO(userService.getInfo(follow.followerId))
            userVO.focus = followService.exists(followingId, userVO.id)
            userVOList.add(userVO)
        }
        return PageImpl<UserVO>(userVOList, page.pageable, page.totalElements)
    }
}