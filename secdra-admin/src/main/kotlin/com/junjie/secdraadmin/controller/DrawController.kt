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
    fun init(path: String, name: String? = "小可爱", introduction: String? = "这是一张很好看的图片，这是我从p站上下载回来的，侵删！"): Boolean {
        try {
            val file = File(path)
            val fileNameList = file.list()
            for (fileName in fileNameList) {
                if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                    val picture = File("$path/$fileName")
                    val read = ImageIO.read(FileInputStream(picture))
                    val draw = Draw()
                    draw.userId = "402880e566ddba740166ddbce0b70000"
                    draw.width = read.width.toLong()
                    draw.height = read.height.toLong()
                    draw.url = fileName
                    draw.name = name
                    draw.introduction = introduction
                    val tag = Tag()
                    tag.name = name
                    val tagList = HashSet<Tag>()
                    tagList.add(tag)
                    draw.tagList = tagList
                    drawService.save(draw)
                }
            }
            return true
        } catch (e: Exception) {
            println(e.message)
            throw e
        }
    }
}
