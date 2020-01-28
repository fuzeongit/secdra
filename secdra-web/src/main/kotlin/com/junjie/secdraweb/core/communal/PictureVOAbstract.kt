package com.junjie.secdraweb.core.communal

import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdradata.index.primary.document.PictureDocument
import com.junjie.secdraservice.service.CollectionService
import com.junjie.secdraservice.service.PictureDocumentService
import com.junjie.secdraweb.vo.PictureVO

abstract class PictureVOAbstract : UserVOAbstract() {
    abstract val pictureDocumentService: PictureDocumentService

    abstract val collectionService: CollectionService

    fun getPictureVO(pictureId: String, userId: String? = null): PictureVO {
        val picture = pictureDocumentService.get(pictureId)
        return getPictureVO(picture, userId)
    }

    fun getPictureVO(picture: PictureDocument, userId: String? = null): PictureVO {
        (picture.privacy == PrivacyState.PRIVATE && picture.userId != userId) && throw PermissionException("你没有权限查看该图片")
        val userVO = super.getUserVO(picture.userId, userId)
        val pictureVO = PictureVO(picture)
        userId?.let { pictureVO.focus = collectionService.exists(it, pictureVO.id) }
        pictureVO.user = userVO
        return pictureVO
    }
}