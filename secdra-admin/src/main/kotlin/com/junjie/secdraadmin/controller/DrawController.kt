package com.junjie.secdraadmin.controller

import com.junjie.secdracollect.constant.TransferState
import com.junjie.secdracollect.dao.PixivDrawDAO
import com.junjie.secdracollect.dao.PixivErrorDAO
import com.junjie.secdracollect.model.PixivDraw
import com.junjie.secdracollect.model.PixivError
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.util.EmojiUtil
import com.junjie.secdraservice.dao.DrawDAO
import com.junjie.secdraservice.document.DrawDocument
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.DrawService
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileInputStream
import java.util.*
import javax.imageio.ImageIO

@RestController
@RequestMapping("draw")
class DrawController(private val drawDAO: DrawDAO, private val pixivDrawDAO: PixivDrawDAO, private val pixivErrorDAO: PixivErrorDAO, private val elasticsearchTemplate: ElasticsearchTemplate, private val drawService: DrawService) {
    @PostMapping("/init")
    fun init(folderPath: String): Any {
        var i = 0
        val errorUrlList = mutableListOf<String>()
        val errorReadList = mutableListOf<String>()
        val userId = "402880e566ddba740166ddbce0b70000"
        val map: HashMap<String, Any> = hashMapOf()
        val fileNameList = File(folderPath).list()
        fileNameList.toList().filter { it.toLowerCase().endsWith(".png") || it.toLowerCase().endsWith(".jpg") || it.toLowerCase().endsWith(".jpeg") }

        for (fileName in fileNameList) {
            val read = try {
                val picture = File("$folderPath/$fileName")
                ImageIO.read(FileInputStream(picture))
            } catch (e: Exception) {
                errorReadList.add(fileName)
                continue
            }
            var draw = Draw()
            draw.userId = userId
            draw.width = read.width.toLong()
            draw.height = read.height.toLong()
            draw.url = fileName
            draw.name = "等待"
            draw.introduction = "这是一张很好看的图片，这是我从p站上下载回来的，侵删！"
            try {
                draw = drawDAO.save(draw)
                val pixivDraw = PixivDraw()
                pixivDraw.pixivId = fileName.split("_")[0];
                pixivDraw.drawId = draw.id
                pixivDraw.state = TransferState.WAIT
                pixivDrawDAO.save(pixivDraw)
                i++
            } catch (e: Exception) {
                errorUrlList.add(fileName)
            }
        }
        map["errorUrlList"] = errorUrlList
        map["errorReadList"] = errorReadList
        map["i"] = i
        return map
    }


    //
//    //保存pixiv采集错误
//    @PostMapping("/pixivError")
//    fun pixivSave(pixivId: String, message: String): PixivError {
//        val pixivError = PixivError()
//        pixivError.pixivId = pixivId
//        pixivError.message = message
//        return pixivErrorDAO.save(pixivError)
//    }
//
//    //从pixiv初始化标签
//    @GetMapping("/initTag")
//    fun initTag(): Boolean {
//        val pixivDrawList = pixivDrawDAO.findAllByInit(false)
//        for (pixivDraw in pixivDrawList) {
//            val drawList = drawDAO.findAllByUrlLike(pixivDraw.pixivId!! + "%")
//            for (draw in drawList) {
//                pixivDraw.init = true
//
//                draw.name = if (pixivDraw.name.isNullOrBlank()) {
//                    draw.name
//                } else {
//                    pixivDraw.name
//                }
//
//                val tagList = pixivDraw.tagList!!.split("|")
//                val tagNameList = draw.tagList.map { it.name }
//                for (addTagName in tagList) {
//                    if (tagNameList.indexOf(addTagName) == -1) {
//                        val tag = Tag()
//                        tag.name = addTagName
//                        draw.tagList.add(tag)
//                    }
//                }
//                for (tag in draw.tagList.toList()) {
//                    if (tagList.indexOf(tag.name) == -1) {
//                        draw.tagList.remove(tag)
//                    }
//                }
//
//                drawDAO.save(draw)
//            }
//
//            pixivDrawDAO.save(pixivDraw)
//
//        }
//        return true
//    }
//

    //获取没有tag的图片
    @GetMapping("/check")
    fun check(): Int {
        val drawList = drawDAO.findAll()
        var i = 0
        for (draw in drawList) {
            if (draw.tagList.size == 0) {
                i++
            }
        }
        return i
    }


    //获取任务
    @GetMapping("/listTagTask")
    fun listTagTask(state: TransferState?): List<PixivDraw> {
        return pixivDrawDAO.findAllByState(state ?: TransferState.WAIT)
    }


    //保存pixiv
    @PostMapping("/pixivDrawSave")
    fun pixivDrawSave(pixivId: String, name: String, userName: String, userId: String, tagString: String): Boolean {
        val pixivDrawList = pixivDrawDAO.findAllByPixivId(pixivId)
        for (pixivDraw in pixivDrawList) {
            if (pixivDraw.state != TransferState.WAIT) continue
            pixivDraw.pixivId = pixivId
            pixivDraw.pixivName = EmojiUtil.emojiChange(name).trim()
            pixivDraw.pixivUserName = EmojiUtil.emojiChange(userName).trim()
            pixivDraw.pixivUserId = userId
            pixivDraw.tagList = EmojiUtil.emojiChange(tagString).trim()
            pixivDraw.state = TransferState.SUCCESS
            try {
                val draw = drawDAO.findById(pixivDraw.drawId!!).orElseThrow { NotFoundException("找不到图片") }
                draw.name = pixivDraw.pixivName
                draw.tagList.toMutableSet().addAll(pixivDraw.tagList!!.split("|").toSet().map { it -> Tag(it) })
                pixivDrawDAO.save(pixivDraw)
                drawDAO.save(draw)
            } catch (e: Exception) {
                val pixivError = PixivError()
                pixivError.pixivId = pixivId
                pixivError.message = e.message
                pixivErrorDAO.save(pixivError)
            }
        }
        return true
    }

    //保存pixiv采集错误
    @PostMapping("/pixivErrorSave")
    fun pixivErrorSave(pixivId: String, message: String): PixivError {
        val pixivError = PixivError()
        pixivError.pixivId = pixivId
        pixivError.message = message
        return pixivErrorDAO.save(pixivError)
    }


    /**
     * 清除重复tag
     */
    @GetMapping("/duplicateRemoval")
    fun duplicateRemoval(): Boolean {

        val list = drawDAO.findAll()
        for (item in list) {
            val tagLsit = item.tagList.distinctBy { it.name }.toSet()
            item.tagList.clear()
            item.tagList.addAll(tagLsit)
            drawDAO.save(item)
        }
        return true
    }


    /**
     * 建立ES索引
     */
    @GetMapping("/initIndex")
    fun initIndex() {
        elasticsearchTemplate.createIndex(DrawDocument::class.java)
    }

    /**
     * 初始化进ES
     */
    @GetMapping("/initEs")
    fun initEs(): Long {
        return drawService.synchronizationIndexDraw()
    }


}
