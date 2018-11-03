package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.dao.IDrawDao
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.IDrawService
import com.qiniu.util.StringUtils
import io.netty.util.internal.StringUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
@RequestMapping("draw")
class DrawController(val drawService: IDrawService, val drawDao: IDrawDao) {
    @GetMapping("/get")
    fun get(id: String?, @CurrentUserId userId: String?): Draw {
        return drawService.get(id!!, userId)
    }

    @GetMapping("/pagingByTag")
    fun get(tag: String?, @PageableDefault(value = 20) pageable: Pageable): Page<Draw> {
        return if (tag != null && !StringUtils.isNullOrEmpty(tag)) {
            drawService.pagingByTag(pageable, tag)
        } else {
            drawService.paging(pageable)
        }
    }

    @PostMapping("/save")
    @Auth
    fun save(@CurrentUserId userId: String, url: String, desc: String, isPrivate: Boolean): Draw {
        return drawService.save(userId, url, desc, isPrivate)
    }


    @GetMapping("/getName")
    fun getName(path: String): Boolean {
        try {
            val file = File(path)
            val fileNameList = file.list()
            for (fileName in fileNameList) {
                if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                    drawService.save("13760029486", fileName)
                }
            }
            return true
        } catch (e: Exception) {
            println(e.message)
            throw e
        }
    }

    @GetMapping("/test")
    fun test(): MutableList<Draw> {
        val draw = Draw()
        draw.userId = "123"
        draw.isPrivate = false
        val tag = Tag()
        draw.tagList = setOf(tag)
        drawDao.save(draw)
        return drawDao.findAll()
    }

    @GetMapping("/test1")
    fun test1(): MutableList<Draw> {
        return drawDao.findAll()
    }


}