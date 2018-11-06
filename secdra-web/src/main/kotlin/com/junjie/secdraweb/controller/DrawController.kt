package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.dao.IDrawDao
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.service.IDrawService
import com.qiniu.util.StringUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
@RequestMapping("/draw")
class DrawController(val drawService: IDrawService, val drawDao: IDrawDao) {
    /**
     * 根据标签获取
     */
    @GetMapping("/pagingByTag")
    fun pagingByTag(name: String?, @PageableDefault(value = 20) pageable: Pageable): Page<Draw> {
        return if (name != null && !StringUtils.isNullOrEmpty(name)) {
            drawService.pagingByTag(pageable, name)
        } else {
            drawService.paging(pageable)
        }
    }

    /**
     * 自己获取
     */
    @Auth
    @GetMapping("/pagingBySelf")
    fun pagingBySelf(@CurrentUserId userId: String, @PageableDefault(value = 20) pageable: Pageable) {
        drawService.pagingByUserId(pageable, userId, true)
    }

    /**
     * 获取他人
     */
    @GetMapping("/pagingByOthers")
    fun pagingByOthers(userId: String, @PageableDefault(value = 20) pageable: Pageable) {
        drawService.pagingByUserId(pageable, userId, false)
    }

    /**
     * 获取图片
     */
    @GetMapping("/get")
    fun get(id: String, @CurrentUserId userId: String?): Draw {
        return drawService.get(id, userId)
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @Auth
    fun update(@CurrentUserId userId: String, drawId: String, desc: String?, isPrivate: Boolean?): Draw {
        return drawService.update(userId, drawId, desc!!, isPrivate!!)
    }

    @GetMapping("/getName")
    fun getName(path: String): Boolean {
        try {
            val file = File(path)
            val fileNameList = file.list()
            for (fileName in fileNameList) {
                if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                    drawService.save("13760029486", fileName, "小可爱")
                }
            }
            return true
        } catch (e: Exception) {
            println(e.message)
            throw e
        }
    }
}