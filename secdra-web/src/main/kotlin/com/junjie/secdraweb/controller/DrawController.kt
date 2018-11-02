package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.dao.IDrawDao
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.service.IDrawService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
@RequestMapping("draw")
class DrawController(val drawService: IDrawService,val drawDao: IDrawDao) {
    @GetMapping("/get")
    fun get(id: String?, @CurrentUserId userId: String?): Draw {
        return drawService.get(id!!, userId)
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
                if(fileName.endsWith(".png")||fileName.endsWith(".jpg"))
                {
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
    fun getName(): MutableList<Draw> {
        return drawDao.findAll()
    }
}