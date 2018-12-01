package com.junjie.secdraadmin.controller

import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.IDrawService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO


@RestController
@RequestMapping("draw")
class DrawController(private var drawService: IDrawService) {
    @PostMapping("/init")
    fun init(path: String): Any {
        var i = 0
        val map = HashMap<String,Array<String>>()
        try {
            val type = File(path)
            val typeNameList = type.list()
            for (typeName in typeNameList) {
                val typePath = "$path/$typeName"
                val file = File(typePath)
                val fileNameList = file.list()
                map[typeName] = fileNameList
                for(fileName in fileNameList){
                    if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                        val picture = File("$typePath/$fileName")
                        val read = ImageIO.read(FileInputStream(picture))
                        val draw = Draw()
                        draw.userId = "402880e566ddba740166ddbce0b70000"
                        draw.width = read.width.toLong()
                        draw.height = read.height.toLong()
                        draw.url = fileName
                        draw.name = typeName
                        draw.introduction = "这是一张很好看的图片，这是我从p站上下载回来的，侵删！"
                        val tag = Tag()
                        tag.name = typeName
                        val tagList = HashSet<Tag>()
                        tagList.add(tag)
                        draw.tagList = tagList
                        drawService.save(draw)
                    }
                    i++
                }
            }
            println(i)
            return  map
        } catch (e: Exception) {
            println(e.message)
            throw e
        }
    }
}
