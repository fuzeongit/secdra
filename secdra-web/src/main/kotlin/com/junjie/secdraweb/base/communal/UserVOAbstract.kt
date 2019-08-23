package com.junjie.secdraweb.base.communal

import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.vo.UserVO

abstract class UserVOAbstract {
    abstract val userService: UserService
    abstract val followService: FollowService

    fun getUserVO(targetId: String, userId: String? = null): UserVO {
        val userVO = UserVO(userService.getInfo(targetId))
        userVO.focus = followService.exists(userId, targetId)
        return userVO
    }

    fun getUserVO(user: User, userId: String? = null): UserVO {
        val userVO = UserVO(user)
        userVO.focus = followService.exists(userId, user.id!!)
        return userVO
    }
}