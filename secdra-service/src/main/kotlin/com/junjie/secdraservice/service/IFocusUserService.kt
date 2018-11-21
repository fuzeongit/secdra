package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.FocusDraw
import com.junjie.secdraservice.model.FocusUser
import org.springframework.stereotype.Service

/**
 * 用户的关注服务
 *
 * @author fjj
 */

interface IFocusUserService {
    fun exists(userId: String?, focusUserId: String): Boolean?

    fun save(userId: String, focusUserId: String): FocusUser

    fun remove(userId: String, focusUserId: String): Boolean
}