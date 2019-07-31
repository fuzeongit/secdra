package com.junjie.secdraadmin.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.junjie.secdrasearch.model.IndexDraw
import com.junjie.secdraservice.dao.DrawDAO
import com.junjie.secdraservice.dao.UserDAO
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.DrawService
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO


@RestController
@RequestMapping("draw")
class DrawController(private var drawDAO: DrawDAO, private var userDAO: UserDAO, private val drawService: DrawService, val elasticsearchTemplate: ElasticsearchTemplate) {
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


    @GetMapping("/initIndex")
    fun initIndex() {
        elasticsearchTemplate.createIndex(IndexDraw::class.java)
    }

    @GetMapping("/initEs")
    fun initEs(): Long {
        return drawService.synchronizationIndexDraw()
    }
}
