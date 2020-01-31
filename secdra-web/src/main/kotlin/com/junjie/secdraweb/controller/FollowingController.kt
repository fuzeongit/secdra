package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.exception.SignInException
import com.junjie.secdradata.constant.FollowState
import com.junjie.secdradata.database.primary.entity.FollowMessage
import com.junjie.secdraservice.service.FollowMessageService
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.core.communal.UserVOAbstract
import com.junjie.secdraweb.service.WebSocketService
import com.junjie.secdraweb.vo.UserVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

/**
 * @author fjj
 * 关注人的控制器
 */
@RestController
@RequestMapping("following")
class FollowingController(private val followMessageService: FollowMessageService,
                          private val webSocketService: WebSocketService,
                          override val userService: UserService,
                          override val followService: FollowService) : UserVOAbstract() {
    @Auth
    @PostMapping("focus")
    @RestfulPack
    fun focus(@CurrentUserId followerId: String, followingId: String): FollowState {
        if (followerId == followingId) {
            throw ProgramException("不能关注自己")
        }
        val focus = followService.exists(followerId, followingId)
        return if (focus == FollowState.STRANGE) {
            val follow = followService.save(followerId, followingId)
            val followMessage = FollowMessage(follow.followerId, follow.followingId)
            followMessageService.save(followMessage)
            webSocketService.sendFollowingFocus(followerId, followingId)
            FollowState.CONCERNED
        } else {
            followService.remove(followerId, followingId)
            FollowState.STRANGE
        }
    }

    /**
     * 取消关注一组
     */
    @Auth
    @PostMapping("unFocus")
    @RestfulPack
    fun unFocus(@CurrentUserId followerId: String, @RequestParam("followingIdList") followingIdList: Array<String>?): Boolean {
        if (followingIdList == null || followingIdList.isEmpty()) {
            throw ProgramException("请选择一个关注")
        }
        for (followingId in followingIdList) {
            try {
                followService.remove(followerId, followingId)
            } catch (e: Exception) {

            }
        }
        return false
    }

    /**
     * 获取关注列表
     */
    @GetMapping("paging")
    @RestfulPack
    fun paging(@CurrentUserId followerId: String?, id: String?, @PageableDefault(value = 20) pageable: Pageable): Page<UserVO> {
        if (followerId.isNullOrEmpty() && id.isNullOrEmpty()) throw SignInException("请重新登录")
        val page = followService.pagingByFollowerId(id ?: followerId!!, pageable)
        val userVOList = page.content.map {
            val userVO = UserVO(userService.get(it.followingId))
            userVO.focus = followService.exists(followerId, userVO.id)
            userVO
        }
        return PageImpl(userVOList, page.pageable, page.totalElements)
    }
}