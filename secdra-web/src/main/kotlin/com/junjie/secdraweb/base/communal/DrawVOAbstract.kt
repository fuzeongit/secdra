package com.junjie.secdraweb.base.communal

import com.junjie.secdraservice.document.DrawDocument
import com.junjie.secdraservice.service.CollectionService
import com.junjie.secdraservice.service.DrawDocumentService
import com.junjie.secdraweb.vo.DrawVO

abstract class DrawVOAbstract : UserVOAbstract() {
    abstract val drawDocumentService: DrawDocumentService

    abstract val collectionService: CollectionService

    fun getDrawVO(drawId: String, userId: String? = null): DrawVO {
        val drawDocument = drawDocumentService.get(drawId)
        val userVO = super.getUserVO(drawDocument.userId, userId)
        val drawVO = DrawVO(drawDocument)
        userId?.let { drawVO.focus = collectionService.exists(it, drawVO.id) }
        drawVO.user = userVO
        return drawVO
    }

    fun getDrawVO(drawDocument: DrawDocument, userId: String? = null): DrawVO {
        val userVO = super.getUserVO(drawDocument.userId, userId)
        val drawVO = DrawVO(drawDocument)
        userId?.let { drawVO.focus = collectionService.exists(it, drawVO.id) }
        drawVO.user = userVO
        return drawVO
    }
}