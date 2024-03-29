package com.junjie.secdraadmin.controller

import com.junjie.secdracollect.service.PixivPictureService
import com.junjie.secdracollect.service.PixivErrorService
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.util.EmojiUtil
import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.collect.entity.PixivPicture
import com.junjie.secdradata.database.collect.entity.PixivError
import com.junjie.secdradata.database.primary.entity.Picture
import com.junjie.secdradata.database.primary.entity.Tag
import com.junjie.secdraservice.service.PictureService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 供插件使用的
 * @author fjj
 */
@RestController
@RequestMapping("collect")
class CollectController(
        private val pictureService: PictureService,
        private val pixivPictureService: PixivPictureService,
        private val pixivErrorService: PixivErrorService) {
    /**
     * 获取采集标签任务
     */
    @GetMapping("listTagTask")
    @RestfulPack
    fun listTagTask(state: TransferState?): List<PixivPicture> {
        return pixivPictureService.listByState(state ?: TransferState.WAIT)
    }

    /**
     * 保存采集
     */
    @PostMapping("save")
    @RestfulPack
    fun pixivPictureSave(pixivId: String, name: String, userName: String, userId: String, tagString: String): Boolean {
        val pixivPictureList = pixivPictureService.listByPixivId(pixivId)
        for (pixivPicture in pixivPictureList) {
            if (pixivPicture.state != TransferState.WAIT) continue
            pixivPicture.pixivId = pixivId
            pixivPicture.pixivName = EmojiUtil.emojiChange(name).trim()
            pixivPicture.pixivUserName = EmojiUtil.emojiChange(userName).trim()
            pixivPicture.pixivUserId = userId
            pixivPicture.tagList = EmojiUtil.emojiChange(tagString).trim()
            pixivPicture.state = TransferState.SUCCESS
            try {
                val picture = pictureService.get(pixivPicture.pictureId)
                picture.name = pixivPicture.pixivName!!
                picture.tagList.addAll(pixivPicture.tagList!!.split("|").asSequence().toSet().asSequence().map { Tag(it) }.toList())
                pixivPictureService.save(pixivPicture)
                pictureService.save(picture, true)
            } catch (e: Exception) {
                pixivErrorService.save(PixivError(pixivId, 500, e.message))
            }
        }
        return true
    }

    /**
     * 保存pixiv采集错误
     */
    @PostMapping("saveError")
    @RestfulPack
    fun pixivErrorSave(pixivId: String, status: Int, message: String?): PixivError {
        val pixivError = PixivError(pixivId, status, message)
        return pixivErrorService.save(pixivError)
    }

    /**
     * 获取套图
     */
    @GetMapping("listPictureBySuit")
    @RestfulPack
    fun listPictureBySuit(pixivId: String): List<Picture> {
        val list = pixivPictureService.listByPixivId(pixivId)
        val resultList = mutableListOf<Picture>()
        for (item in list) {
            try {
                resultList.add(pictureService.getByLife(item.pictureId))
            } catch (e: NotFoundException) {
            }
        }
        return resultList
    }

    /**
     * 保存pixiv采集错误
     */
    @PostMapping("giveUpError")
    @RestfulPack
    fun giveUpError(status: Int?): Boolean {
        val pixivErrorList = pixivErrorService.listByRecord(false, status)
        pixivErrorList.forEach {
            val pixivPictureList =
                    pixivPictureService.listByPixivId(it.pixivId)
            pixivPictureService.saveAll(pixivPictureList.map { pixivPicture ->
                pixivPicture.state = TransferState.ABANDON
                pixivPicture
            })
            it.record = true
            pixivErrorService.save(it)
        }
        return true
    }
}