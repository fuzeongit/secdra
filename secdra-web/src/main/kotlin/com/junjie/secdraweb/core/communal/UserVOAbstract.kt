package com.junjie.secdraweb.core.communal

import com.junjie.secdradata.database.primary.entity.User
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.vo.UserVO

abstract class UserVOAbstract {
    abstract val userService: UserService
    abstract val followService: FollowService

    fun getUserVO(targetId: String, userId: String? = null): UserVO {
        return getUserVO(userService.getInfo(targetId),userId)
    }

    fun getUserVO(user: User, userId: String? = null): UserVO {
        val userVO = UserVO(user)
        userVO.focus = followService.exists(userId, user.id!!)
        return userVO
    }
}