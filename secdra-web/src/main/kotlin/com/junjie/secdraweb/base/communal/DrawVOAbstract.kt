package com.junjie.secdraweb.base.communal

import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.document.DrawDocument
import com.junjie.secdraservice.service.CollectionService
import com.junjie.secdraservice.service.DrawDocumentService
import com.junjie.secdraweb.vo.DrawVO

abstract class DrawVOAbstract : UserVOAbstract() {
    abstract val drawDocumentService: DrawDocumentService

    abstract val collectionService: CollectionService

    fun getDrawVO(drawId: String, userId: String? = null): DrawVO {
        val draw = drawDocumentService.get(drawId)
        return getDrawVO(draw, userId)
    }

    fun getDrawVO(draw: DrawDocument, userId: String? = null): DrawVO {
        (draw.privacy == PrivacyState.PRIVATE && draw.userId != userId) && throw PermissionException("你没有权限查看该图片")
        val userVO = super.getUserVO(draw.userId, userId)
        val drawVO = DrawVO(draw)
        userId?.let { drawVO.focus = collectionService.exists(it, drawVO.id) }
        drawVO.user = userVO
        return drawVO
    }
}