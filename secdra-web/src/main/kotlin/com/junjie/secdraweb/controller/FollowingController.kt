package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.service.IFollowService
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.vo.UserVo
import org.springframework.beans.BeanUtils
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
@RequestMapping("/following")
class FollowingController(private val followService: IFollowService, private val userService: IUserService) {
    @Auth
    @PostMapping("/focus")
    fun focus(@CurrentUserId followerId: String, followingId: String): Boolean {
        if (followerId == followingId) {
            throw ProgramException("不能关注自己")
        }
        val isFocus = followService.exists(followerId, followingId) ?: throw ProgramException("不能关注")
        return if (!isFocus) {
            followService.save(followerId, followingId)
            true
        } else {
            followService.remove(followerId, followingId)
            false
        }
    }

    /**
     * 取消关注一组
     */
    @Auth
    @PostMapping("/unFocus")
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
        return false;
    }

    /**
     * 获取关注列表
     */
    @Auth
    @GetMapping("/paging")
    fun paging(@CurrentUserId followerId: String, id: String?, @PageableDefault(value = 20) pageable: Pageable): Page<UserVo> {
        val page = followService.paging(
                if (id.isNullOrEmpty()) {
                    followerId
                } else {
                    id!!
                }, pageable)
        val userVoList = ArrayList<UserVo>()
        for (follow in page.content) {
            val userVo = UserVo(userService.getInfo(follow.followingId!!))
            userVo.isFocus = followService.exists(followerId, userVo.id!!)
            userVoList.add(userVo)
        }
        return PageImpl<UserVo>(userVoList, page.pageable, page.totalElements)
    }
}