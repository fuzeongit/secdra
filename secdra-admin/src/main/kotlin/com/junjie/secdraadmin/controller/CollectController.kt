package com.junjie.secdraadmin.controller

import com.junjie.secdracollect.service.PixivDrawService
import com.junjie.secdracollect.service.PixivErrorService
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.util.EmojiUtil
import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.collect.entity.PixivDraw
import com.junjie.secdradata.database.collect.entity.PixivError
import com.junjie.secdradata.database.primary.entity.Tag
import com.junjie.secdraservice.service.DrawService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 供插件使用的
 * @author fjj
 */
@RestController
@RequestMapping("controller")
class CollectController(
        private val drawService: DrawService,
        private val pixivDrawService: PixivDrawService,
        private val pixivErrorService: PixivErrorService) {
    /**
     * 获取采集标签任务
     */
    @GetMapping("/listTagTask")
    @RestfulPack
    fun listTagTask(state: TransferState?): List<PixivDraw> {
        return pixivDrawService.listByState(state ?: TransferState.WAIT)
    }

    /**
     * 保存采集
     */
    @PostMapping("/save")
    @RestfulPack
    fun pixivDrawSave(pixivId: String, name: String, userName: String, userId: String, tagString: String): Boolean {
        val pixivDrawList = pixivDrawService.listByPixivId(pixivId)
        for (pixivDraw in pixivDrawList) {
            if (pixivDraw.state != TransferState.WAIT) continue
            pixivDraw.pixivId = pixivId
            pixivDraw.pixivName = EmojiUtil.emojiChange(name).trim()
            pixivDraw.pixivUserName = EmojiUtil.emojiChange(userName).trim()
            pixivDraw.pixivUserId = userId
            pixivDraw.tagList = EmojiUtil.emojiChange(tagString).trim()
            pixivDraw.state = TransferState.SUCCESS
            try {
                val draw = drawService.get(pixivDraw.drawId)
                draw.name = pixivDraw.pixivName!!
                draw.tagList.addAll(pixivDraw.tagList!!.split("|").asSequence().toSet().asSequence().map { it -> Tag(it) }.toList())
                pixivDrawService.save(pixivDraw)
                drawService.save(draw)
            } catch (e: Exception) {
                pixivErrorService.save(PixivError(pixivId, e.message))
            }
        }
        return true
    }

    /**
     * 保存pixiv采集错误
     */
    @PostMapping("/saveError")
    @RestfulPack
    fun pixivErrorSave(pixivId: String, message: String): PixivError {
        val pixivError = PixivError(pixivId, message)
        return pixivErrorService.save(pixivError)
    }
}