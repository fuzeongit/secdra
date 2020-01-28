package com.junjie.secdraadmin.controller

import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdraadmin.code.communal.CommonAbstract
import com.junjie.secdraadmin.vo.PictureInitVO
import com.junjie.secdracollect.service.PixivPictureService
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.collect.entity.PixivPicture
import com.junjie.secdradata.database.primary.entity.Picture
import com.junjie.secdradata.database.primary.entity.User
import com.junjie.secdradata.index.primary.document.PictureDocument
import com.junjie.secdraservice.service.PictureService
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
@RequestMapping("picture")
class PictureController(
        override val accountService: AccountService,
        override val userService: UserService,
        private val pictureService: PictureService,
        private val pixivPictureService: PixivPictureService,
        private val elasticsearchTemplate: ElasticsearchTemplate) : CommonAbstract() {

    @PostMapping("/init")
    @RestfulPack
    fun init(folderPath: String): PictureInitVO {
        var readNumber = 0
        val errorUrlList = mutableListOf<String>()
        val errorReadList = mutableListOf<String>()
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
            //TODO
            val picture = Picture(User(), fileName, read.width.toLong(), read.height.toLong(), fileName, "这是一张很好看的图片，这是我从p站上下载回来的，侵删！")
            try {
                val pictureDocument = pictureService.save(picture)
                val pixivPicture = PixivPicture(fileName.split("_")[0], pictureDocument.id!!)
                pixivPictureService.save(pixivPicture)
                readNumber++
            } catch (e: Exception) {
                errorUrlList.add(fileName)
            }
        }
        return PictureInitVO(errorUrlList, errorReadList, readNumber)
    }

    /**
     * 获取没有tag的图片数量
     */
    @GetMapping("/checkTag")
    @RestfulPack
    fun checkTag(): Int {
        val pictureList = pictureService.list()
        var nullTagNumber = 0
        for (picture in pictureList) {
            if (picture.tagList.size == 0) {
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
        val list = pictureService.list()
        for (item in list) {
            val tagList = item.tagList.asSequence().distinctBy { it.name }.toSet()
            item.tagList.clear()
            item.tagList.addAll(tagList)
            pictureService.save(item)
        }
        return true
    }

    /**
     * 绑定user
     */
    @PostMapping("/bindUser")
    @RestfulPack
    fun bindUser(): Boolean {
        val pictureList = pictureService.listByUserId("")
        for (picture in pictureList) {
            val pixivPicture = pixivPictureService.getByPictureId(picture.id!!)
            if (pixivPicture.state == TransferState.SUCCESS) {
                val user = try {
                    val accountToPixivUser = pixivPictureService.getAccountByPixivUserId(pixivPicture.pixivUserId!!)
                    userService.getByAccountId(accountToPixivUser.accountId)
                } catch (e: NotFoundException) {
                    val user = initUser()
                    pixivPictureService.saveAccount(user.accountId, pixivPicture.pixivUserId!!)
                    user
                }
                picture.user = user
                pictureService.save(picture)
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
        elasticsearchTemplate.createIndex(PictureDocument::class.java)
        return true
    }

    /**
     * 初始化进ES
     */
    @PostMapping("/initEs")
    @RestfulPack
    fun initEs(): Long {
        return pictureService.synchronizationIndexPicture()
    }
}
