package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.service.IFollowerService
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.vo.UserVo
import com.qiniu.util.StringUtils
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
 * 关注人的控制器
 */
@RestController
@RequestMapping("/follower")
class FollowerController(private val followerService: IFollowerService, private val userService: IUserService) {
    @Auth
    @PostMapping("/focus")
    fun focus(@CurrentUserId userId: String, followerId: String): Boolean {
        var flag = false
        val isFocus = followerService.exists(userId, followerId) ?: throw ProgramException("不能关注")
        flag = if (!isFocus) {
            followerService.save(userId, followerId)
            true
        } else {
            followerService.remove(userId, followerId)
            false
        }
        return flag;
    }

    /**
     * 获取关注列表
     */
    @Auth
    @GetMapping("/paging")
    fun paging(@CurrentUserId userId: String, id: String?, @PageableDefault(value = 20) pageable: Pageable): Page<UserVo> {
        val page = if (StringUtils.isNullOrEmpty(id)) {
            followerService.paging(userId, pageable)
        } else {
            followerService.paging(id!!, pageable)
        }
        val userVoList = ArrayList<UserVo>()
        for (follower in page.content) {
            val user = userService.getInfo(follower.followerId!!)
            val userVo = UserVo()
            BeanUtils.copyProperties(user, userVo)
            if (StringUtils.isNullOrEmpty(id)) {
                userVo.isFocus = true;
            } else {
                userVo.isFocus = followerService.exists(userId, userVo.id!!)
            }
            userVoList.add(userVo)
        }
        return PageImpl<UserVo>(userVoList, page.pageable, page.totalElements)
    }
}