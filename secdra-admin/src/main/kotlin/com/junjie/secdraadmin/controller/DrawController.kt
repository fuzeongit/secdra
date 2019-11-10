package com.junjie.secdraadmin.controller

import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdraadmin.code.communal.CommonAbstract
import com.junjie.secdraadmin.vo.DrawInitVO
import com.junjie.secdracollect.service.PixivDrawService
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.collect.entity.PixivDraw
import com.junjie.secdradata.database.primary.entity.Draw
import com.junjie.secdradata.index.primary.document.DrawDocument
import com.junjie.secdraservice.service.DrawService
import com.junjie.secdraservice.service.UserService
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO

@RestController
@RequestMapping("draw")
class DrawController(
        override val accountService: AccountService,
        override val userService: UserService,
        private val drawService: DrawService,
        private val pixivDrawService: PixivDrawService,
        private val elasticsearchTemplate: ElasticsearchTemplate) : CommonAbstract() {

    @PostMapping("/init")
    @RestfulPack
    fun init(folderPath: String, phone: String?): DrawInitVO {
        var readNumber = 0
        val errorUrlList = mutableListOf<String>()
        val errorReadList = mutableListOf<String>()
        val userId = if (phone != null) {
            val account = accountService.getByPhone(phone)
            userService.getByAccountId(account.id!!).id!!
        } else {
            val userList = userService.list()
            userList.isEmpty() && throw ProgramException("用户列表为空，请先初始化用户")
            userList.shuffled().last().id!!
        }
        val fileNameList = File(folderPath).list() ?: arrayOf()
        fileNameList.toList().filter { it.toLowerCase().endsWith(".png") || it.toLowerCase().endsWith(".jpg") || it.toLowerCase().endsWith(".jpeg") }

        for (fileName in fileNameList) {
            val read = try {
                val picture = File("$folderPath/$fileName")
                ImageIO.read(FileInputStream(picture))
            } catch (e: Exception) {
                errorReadList.add(fileName)
                continue
            }
            val draw = Draw(userId, fileName, read.width.toLong(), read.height.toLong(), fileName, "这是一张很好看的图片，这是我从p站上下载回来的，侵删！")
            try {
                val drawDocument = drawService.save(draw)
                val pixivDraw = PixivDraw(fileName.split("_")[0], drawDocument.id!!)
                pixivDrawService.save(pixivDraw)
                readNumber++
            } catch (e: Exception) {
                errorUrlList.add(fileName)
            }
        }
        return DrawInitVO(errorUrlList, errorReadList, readNumber)
    }

    /**
     * 获取没有tag的图片数量
     */
    @GetMapping("/checkTag")
    @RestfulPack
    fun checkTag(): Int {
        val drawList = drawService.list()
        var nullTagNumber = 0
        for (draw in drawList) {
            if (draw.tagList.size == 0) {
                nullTagNumber++
            }
        }
        return nullTagNumber
    }

    /**
     * 清除重复tag
     */
    @PostMapping("/duplicateRemoval")
    @RestfulPack
    fun duplicateRemoval(): Boolean {
        val list = drawService.list()
        for (item in list) {
            val tagList = item.tagList.asSequence().distinctBy { it.name }.toSet()
            item.tagList.clear()
            item.tagList.addAll(tagList)
            drawService.save(item)
        }
        return true
    }

    /**
     * 绑定user
     */
    @PostMapping("/bindUser")
    @RestfulPack
    fun bindUser(): Boolean {
        val drawList = drawService.list()
        for (draw in drawList) {
            val pixivDraw = pixivDrawService.getByDrawId(draw.id!!)
            if (pixivDraw.state == TransferState.SUCCESS) {
                val user = try {
                    val accountToPixivUser = pixivDrawService.getAccountByPixivUserId(pixivDraw.pixivUserId!!)
                    userService.getByAccountId(accountToPixivUser.accountId)
                } catch (e: NotFoundException) {
                    val user = initUser()
                    pixivDrawService.saveAccount(user.accountId, pixivDraw.pixivUserId!!)
                    user
                }
                draw.userId = user.id!!
                drawService.save(draw)
            }
        }
        return true
    }

    /**
     * 建立ES索引
     */
    @PostMapping("/initIndex")
    @RestfulPack
    fun initIndex(): Boolean {
        elasticsearchTemplate.createIndex(DrawDocument::class.java)
        return true
    }

    /**
     * 初始化进ES
     */
    @PostMapping("/initEs")
    @RestfulPack
    fun initEs(): Long {
        return drawService.synchronizationIndexDraw()
    }
}
