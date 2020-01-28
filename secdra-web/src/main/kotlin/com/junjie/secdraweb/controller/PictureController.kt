package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdradata.index.primary.document.PictureDocument
import com.junjie.secdradata.database.primary.entity.Picture
import com.junjie.secdradata.database.primary.entity.Tag
import com.junjie.secdraservice.service.*
import com.junjie.secdraweb.core.communal.PictureVOAbstract
import com.junjie.secdraweb.core.component.BaseConfig
import com.junjie.secdraweb.service.QiniuComponent
import com.junjie.secdraweb.vo.PictureVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * @author fjj
 * 画像的控制器
 */
@RestController
@RequestMapping("picture")
class PictureController(private val pictureService: PictureService,
                        private val qiniuComponent: QiniuComponent,
                        private val baseConfig: BaseConfig,
                        override val pictureDocumentService: PictureDocumentService,
                        override val collectionService: CollectionService,
                        override val userService: UserService,
                        override val followService: FollowService) : PictureVOAbstract() {
    /**
     * 根据标签获取
     */
    @GetMapping("/paging")
    @RestfulPack
    fun paging(@CurrentUserId userId: String?, @PageableDefault(value = 20) pageable: Pageable, tagList: String?, precise: Boolean?, name: String?, startDate: Date?, endDate: Date?, targetId: String?): Page<PictureVO> {
        return getPageVO(pictureDocumentService.paging(pageable, tagList?.split(" "), precise != null && precise, name, startDate, endDate, targetId, targetId == userId), userId)
    }

    /**
     * 获取推荐
     */
    @GetMapping("/pagingByRecommend")
    @RestfulPack
    fun pagingByRecommend(@CurrentUserId userId: String?, @PageableDefault(value = 20) pageable: Pageable, startDate: Date?, endDate: Date?): Page<PictureVO> {
        return getPageVO(pictureDocumentService.pagingByRecommend(userId, pageable, startDate, endDate), userId)
    }

    /**
     * 获取图片
     */
    @GetMapping("/get")
    @RestfulPack
    fun get(id: String, @CurrentUserId userId: String?): PictureVO {
        return getPictureVO(id, userId)
    }

    /**
     * 获取tag第一张
     * 会移到ES搜索
     */
    @GetMapping("getFirstByTag")
    @RestfulPack
    fun getFirstByTag(tag: String): PictureVO {
        return getPictureVO(pictureDocumentService.getFirstByTag(tag))
    }

    /**
     * 按tag统计图片
     */
    @GetMapping("countByTag")
    @RestfulPack
    fun countByTag(tag: String): Long {
        return pictureDocumentService.countByTag(tag)
    }

    /**
     * 保存图片
     */
    @Auth
    @PostMapping("/save")
    @RestfulPack
    fun save(@CurrentUserId userId: String, url: String, name: String, introduction: String?, privacy: PrivacyState, @RequestParam("tagList") tagList: Set<String>?): PictureVO {
        val picture = Picture(userService.get(userId), url, name, introduction!!)
        picture.privacy = privacy
        if (tagList != null && tagList.isNotEmpty()) {
            picture.tagList.addAll(tagList.map { Tag(it) })
        }

        qiniuComponent.move(url, baseConfig.qiniuBucket)
        val imageInfo = qiniuComponent.getImageInfo(url, baseConfig.qiniuBucketUrl) ?: throw ProgramException("移除图片出错")
        picture.width = imageInfo.width
        picture.height = imageInfo.height
        return getPictureVO(pictureService.save(picture), userId)
    }

    /**
     * 更新
     */
    @Auth
    @PostMapping("/update")
    @RestfulPack
    fun update(@CurrentUserId userId: String, id: String, name: String?, introduction: String?, privacy: PrivacyState, @RequestParam("tagList") tagList: Array<String>?): PictureVO {
        val picture = pictureService.get(id)
        picture.user?.id != userId && throw PermissionException("您无权修改该图片")
        if (name != null && name.isNotEmpty()) picture.name = name
        if (introduction != null && introduction.isNotEmpty()) picture.introduction = introduction
        picture.privacy = privacy
        if (tagList != null && tagList.isNotEmpty()) {
            val tagNameList = picture.tagList.map { it.name }
            val sourceTagList = picture.tagList.toMutableSet()
            for (addTagName in tagList) {
                if (tagNameList.indexOf(addTagName) == -1) {
                    sourceTagList.add(Tag(addTagName))
                }
            }
            for (tag in picture.tagList.toList()) {
                if (tagList.indexOf(tag.name) == -1) {
                    sourceTagList.remove(tag)
                }
            }
            picture.tagList.clear()
            picture.tagList.addAll(sourceTagList)
        }
        return getPictureVO(pictureService.save(picture), userId)
    }

    /**
     * 移除图片
     */
    @Auth
    @PostMapping("/remove")
    @RestfulPack
    fun remove(@CurrentUserId userId: String, id: String): Boolean {
        val picture = pictureService.get(id)
        if (userId != picture.user?.id) {
            throw PermissionException("你无权删除该图片")
        }
        qiniuComponent.move(picture.url, baseConfig.qiniuTempBucket, baseConfig.qiniuBucket)
        return pictureService.remove(picture.id!!)
    }

    @Auth
    @PostMapping("/batchUpdate")
    @RestfulPack
    fun batchUpdate(@CurrentUserId userId: String, idList: Array<String>, name: String?, introduction: String?, privacy: PrivacyState?, @RequestParam("tagList") tagList: Array<String>?): MutableList<PictureDocument> {
        val pictureList = mutableListOf<PictureDocument>()
        for (id in idList) {
            try {
                val picture = pictureService.get(id)
                if (userId != picture.user?.id) {
                    continue
                }
                if (name != null && name.isNotEmpty()) picture.name = name
                if (introduction != null && introduction.isNotEmpty()) picture.introduction = introduction
                privacy?.let { picture.privacy = it }
                if (tagList != null && tagList.isNotEmpty()) {
                    val sourceTagList = picture.tagList.toMutableSet()
                    sourceTagList.addAll(tagList.map { Tag(it) })
                    picture.tagList.clear()
                    picture.tagList.addAll(sourceTagList.distinctBy { it.name })
                }
                pictureList.add(pictureService.save(picture))
            } catch (e: Exception) {
                println(e.message)
            }
        }
        return pictureList
    }

    private fun getPageVO(page: Page<PictureDocument>, userId: String? = null): Page<PictureVO> {
        val pictureVOList = page.content.map { getPictureVO(it, userId) }
        return PageImpl(pictureVOList, page.pageable, page.totalElements)
    }
}