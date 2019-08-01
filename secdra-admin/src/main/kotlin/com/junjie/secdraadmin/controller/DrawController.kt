package com.junjie.secdraadmin.controller

import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdrasearch.model.DrawDocument
import com.junjie.secdraservice.dao.DrawDAO
import com.junjie.secdraservice.dao.PixivDrawDAO
import com.junjie.secdraservice.dao.PixivErrorDAO
import com.junjie.secdraservice.dao.UserDAO
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.PixivDraw
import com.junjie.secdraservice.model.PixivError
import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.DrawService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.io.File
import java.io.FileInputStream
import java.util.*
import javax.imageio.ImageIO


@RestController
@RequestMapping("draw")
class DrawController(private var drawDAO: DrawDAO, private var userDAO: UserDAO, private val drawService: DrawService, private val pixivDrawDAO: PixivDrawDAO, private val pixivErrorDAO: PixivErrorDAO, private val elasticsearchTemplate: ElasticsearchTemplate) {
    @PostMapping("/init")
    fun init(folderPath: String): Any {
        var i = 0
        val map = HashMap<String, Array<String>>()
        val userList = userDAO.findAll()
        try {
            val typeNameList = File(folderPath).list()
            for (typeName in typeNameList) {
                val typePath = "$folderPath/$typeName"
                val fileNameList = File(typePath).list()
                fileNameList.toList().filter { it.toLowerCase().endsWith(".png") || it.toLowerCase().endsWith(".jpg") || it.toLowerCase().endsWith(".jpeg") }
                map[typeName] = fileNameList
                for (fileName in fileNameList) {
                    val picture = File("$typePath/$fileName")
                    val read = ImageIO.read(FileInputStream(picture))
                    val draw = Draw()
                    draw.userId = userList.shuffled().first().id
                    draw.width = read.width.toLong()
                    draw.height = read.height.toLong()
                    draw.url = fileName
                    draw.name = typeName
                    draw.introduction = "这是一张很好看的图片，这是我从p站上下载回来的，侵删！"
                    val tag = Tag()
                    tag.name = typeName
                    val tagList = mutableSetOf<Tag>()
                    tagList.add(tag)
                    draw.tagList = tagList
                    drawDAO.save(draw)
                    i++
                }
            }
            println(i)
            return map
        } catch (e: Exception) {
            println(e.message)
            throw e
        }
    }


    @PostMapping("/pixivSave")
    fun pixivSave(drawId: String,name: String,userName: String,userId: String, tagList: String): PixivDraw {
        val p = PixivDraw()
        p.drawId = drawId
        p.name = name
        p.userName = userName
        p.userId = userId
        p.tagList = tagList
        return pixivDrawDAO.save(p)
    }

    @PostMapping("/pixivError")
    fun pixivSave(drawId: String, message: String): PixivError {
        val pixivError = PixivError()
        pixivError.drawId = drawId
        pixivError.message = message
        return pixivErrorDAO.save(pixivError)
    }

    /**
     * 根据标签获取
     */
    @GetMapping("/paging")
    fun paging(@CurrentUserId userId: String?, tag: String?, @PageableDefault(value = 20) pageable: Pageable, startDate: Date?, endDate: Date?): Page<Draw> {
        return drawService.paging(pageable, tag, startDate, endDate)
    }

    @GetMapping("/initIndex")
    fun initIndex() {
        elasticsearchTemplate.createIndex(DrawDocument::class.java)
    }

    @GetMapping("/initEs")
    fun initEs(): Long {
        return drawService.synchronizationIndexDraw()
    }
}
