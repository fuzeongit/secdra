package com.junjie.secdraadmin.controller

import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdraadmin.core.communal.CommonAbstract
import com.junjie.secdraadmin.vo.PictureInitVO
import com.junjie.secdracollect.service.PixivPictureService
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdradata.constant.PictureLifeState
import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.collect.entity.PixivPicture
import com.junjie.secdradata.database.primary.entity.Picture
import com.junjie.secdradata.index.primary.document.PictureDocument
import com.junjie.secdraqiniu.core.component.QiniuConfig
import com.junjie.secdraqiniu.service.BucketService
import com.junjie.secdraservice.service.PictureService
import com.junjie.secdraservice.service.UserService
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
@RequestMapping("picture")
class PictureController(
        override val accountService: AccountService,
        override val userService: UserService,
        private val bucketService: BucketService,
        private val qiniuConfig: QiniuConfig,
        private val pictureService: PictureService,
        private val pixivPictureService: PixivPictureService,
        private val elasticsearchTemplate: ElasticsearchTemplate) : CommonAbstract() {

    /**
     * 根据标签获取
     */
    @GetMapping("paging")
    @RestfulPack
    fun paging(userId: String?, phone: String?, name: String?, privacy: PrivacyState?, life: PictureLifeState?, master: Boolean?, startDate: Date?, endDate: Date?, @PageableDefault(value = 20) pageable: Pageable): Page<Picture> {
        val phoneUserId = if (!phone.isNullOrEmpty() && userId.isNullOrEmpty()) {
            val account = accountService.getByPhone(phone!!)
            val user = userService.getByAccountId(account.id!!)
            user.id
        } else null
        return pictureService.paging(pageable, if (!userId.isNullOrEmpty()) userId else phoneUserId, name, privacy, life, master, startDate, endDate)
    }

    /**
     * 更新隐藏状态
     */
    @PostMapping("updatePrivacy")
    @RestfulPack
    fun updatePrivacy(id: String): Boolean {
        val picture = pictureService.get(id)
        picture.privacy = when (picture.privacy) {
            PrivacyState.PRIVATE -> PrivacyState.PUBLIC
            PrivacyState.PUBLIC -> PrivacyState.PRIVATE
        }
        pictureService.save(picture)
        return true
    }

    /**
     * 逻辑删除图片
     */
    @PostMapping("batchRemove")
    @RestfulPack
    fun batchRemove(@RequestParam("idList") idList: Array<String>): Boolean {
        for (id in idList) {
            val picture = pictureService.get(id)
            pictureService.remove(picture)
        }
        return true
    }

    /**
     * 还原
     */
    @PostMapping("reduction")
    @RestfulPack
    fun reduction(@RequestParam("idList") idList: Array<String>): Boolean {
        for (id in idList) {
            pictureService.reduction(id)
        }
        return true
    }

    /**
     * 物理删除图片
     * 慎用
     */
    @PostMapping("batchDelete")
    @RestfulPack
    fun batchDelete(@RequestParam("idList") idList: Array<String>): Boolean {
        for (id in idList) {
            val picture = pictureService.getByLife(id)
            bucketService.move(picture.url, qiniuConfig.qiniuTempBucket, qiniuConfig.qiniuBucket)
            pictureService.delete(id)
        }
        return true
    }


    /**
     * 根据文件夹写入图片写入数据库
     * 本地使用
     */
    @PostMapping("init")
    @RestfulPack
    fun init(folderPath: String, userId: String, privacy: PrivacyState): PictureInitVO {
        var readNumber = 0
        val errorUrlList = mutableListOf<String>()
        val errorReadList = mutableListOf<String>()
        val fileNameList = File(folderPath).list() ?: arrayOf()
        fileNameList.toList().filter { it.toLowerCase().endsWith(".png") || it.toLowerCase().endsWith(".jpg") || it.toLowerCase().endsWith(".jpeg") }
        //绑定到临时id
        val user = userService.get(userId)
        for (fileName in fileNameList) {
            val read = try {
                val picture = File("$folderPath/$fileName")
                ImageIO.read(FileInputStream(picture))
            } catch (e: Exception) {
                errorReadList.add(fileName)
                continue
            }
            val picture = Picture(user, fileName, read.width.toLong(), read.height.toLong(), fileName, "这是一张很好看的图片，这是我从p站上下载回来的，侵删！", privacy)
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
     * 绑定user
     */
    @PostMapping("bindUser")
    @RestfulPack
    fun bindUser(userId: String): Boolean {
        //临时id
        val pictureList = pictureService.listByUserId(userId)
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
    @PostMapping("initIndex")
    @RestfulPack
    fun initIndex(): Boolean {
        elasticsearchTemplate.createIndex(PictureDocument::class.java)
        return true
    }

    /**
     * 初始化进ES
     */
    @PostMapping("importES")
    @RestfulPack
    fun importES(): Long {
        return pictureService.synchronizationIndexPicture()
    }
}
