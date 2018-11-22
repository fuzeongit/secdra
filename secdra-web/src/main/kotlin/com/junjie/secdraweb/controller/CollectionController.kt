package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.service.IDrawService
import com.junjie.secdraservice.service.ICollectionService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author fjj
 * 图片收藏的
 */
@RestController
@RequestMapping("/collection")
class CollectionController(private val collectionService: ICollectionService, private val drawService: IDrawService) {
    @Auth
    @PostMapping("/focus")
    fun focus(@CurrentUserId userId: String, drawId: String): Boolean {
        val draw = drawService.get(drawId, userId)
        var flag = false
        flag = if (!collectionService.exists(userId, drawId)) {
            collectionService.save(userId, drawId)
            true
        } else {
            collectionService.remove(userId, drawId)
            false
        }
        drawService.update(drawId, null, collectionService.countByDrawId(drawId))
        return flag;
    }
}