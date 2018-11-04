package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.service.IFocusDrawService
import com.qiniu.util.StringUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 图片收藏的
 */
@RestController
@RequestMapping("/focusDraw")
class FocusDrawController(private val focusDrawService: IFocusDrawService) {
    @Auth
    @PostMapping("/focus")
    fun focus(@CurrentUserId userId: String, drawId: String): Boolean {
        val focusDraw = focusDrawService.get(userId, drawId)
        if (StringUtils.isNullOrEmpty(focusDraw.id)) {
            focusDrawService.save(userId, drawId)
        } else {
            focusDrawService.remove(userId, drawId)
        }
        return true;
    }
}