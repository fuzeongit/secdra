package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.service.IDrawService
import com.junjie.secdraservice.service.IFocusDrawService
import com.junjie.secdraservice.service.IFocusUserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author fjj
 * 图片收藏的
 */
@RestController
@RequestMapping("/focusUser")
class FocusUserController(private val focusUserService: IFocusUserService) {
    @Auth
    @PostMapping("/focus")
    fun focus(@CurrentUserId userId: String, focusUserId: String): Boolean {
        var flag = false
        val isFocus = focusUserService.exists(userId, focusUserId) ?: throw ProgramException("不能关注")
        flag = if (!isFocus) {
            focusUserService.save(userId, focusUserId)
            true
        } else {
            focusUserService.remove(userId, focusUserId)
            false
        }
        return flag;
    }
}