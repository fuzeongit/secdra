package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.service.IDrawService
import com.junjie.secdraservice.service.IFocusDrawService
import com.qiniu.util.StringUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author fjj
 * 图片收藏的
 */
@RestController
@RequestMapping("/focusDraw")
class FocusDrawController(private val focusDrawService: IFocusDrawService, private val drawService: IDrawService) {
    @Auth
    @PostMapping("/focus")
    fun focus(@CurrentUserId userId: String, drawId: String): Boolean {
        val draw = drawService.get(drawId, userId)
        var flag = false
        flag = if (!focusDrawService.exists(userId, drawId)) {
            focusDrawService.save(userId, drawId)
            true
        } else {
            focusDrawService.remove(userId, drawId)
            false
        }
        drawService.update(drawId, null, focusDrawService.countByDrawId(drawId))
        return flag;
    }
}