package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdradata.index.primary.document.DrawDocument
import com.junjie.secdradata.database.primary.entity.Draw
import com.junjie.secdradata.database.primary.entity.Tag
import com.junjie.secdraservice.service.*
import com.junjie.secdraweb.core.communal.DrawVOAbstract
import com.junjie.secdraweb.core.component.BaseConfig
import com.junjie.secdraweb.service.QiniuComponent
import com.junjie.secdraweb.vo.DrawVO
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
@RequestMapping("draw")
class DrawController(private val drawService: DrawService,
                     private val qiniuComponent: QiniuComponent,
                     private val baseConfig: BaseConfig,
                     override val drawDocumentService: DrawDocumentService,
                     override val collectionService: CollectionService,
                     override val userService: UserService,
                     override val followService: FollowService) : DrawVOAbstract() {
    /**
     * 根据标签获取
     */
    @GetMapping("/paging")
    @RestfulPack
    fun paging(@CurrentUserId userId: String?, @PageableDefault(value = 20) pageable: Pageable, tagList: String?, precise: Boolean?, name: String?, startDate: Date?, endDate: Date?, targetId: String?): Page<DrawVO> {
        return getPageVO(drawDocumentService.paging(pageable, tagList?.split(" "), precise != null && precise, name, startDate, endDate, targetId, targetId == userId), userId)
    }

    /**
     * 获取推荐
     */
    @GetMapping("/pagingByRecommend")
    @RestfulPack
    fun pagingByRecommend(@CurrentUserId userId: String?, @PageableDefault(value = 20) pageable: Pageable, startDate: Date?, endDate: Date?): Page<DrawVO> {
        return getPageVO(drawDocumentService.pagingByRecommend(userId, pageable, startDate, endDate), userId)
    }

    /**
     * 获取图片
     */
    @GetMapping("/get")
    @RestfulPack
    fun get(id: String, @CurrentUserId userId: String?): DrawVO {
        return getDrawVO(id, userId)
    }

    /**
     * 获取tag第一张
     * 会移到ES搜索
     */
    @GetMapping("getFirstByTag")
    @RestfulPack
    fun getFirstByTag(tag: String): DrawVO {
        return getDrawVO(drawDocumentService.getFirstByTag(tag))
    }

    /**
     * 按tag统计图片
     */
    @GetMapping("countByTag")
    @RestfulPack
    fun countByTag(tag: String): Long {
        return drawDocumentService.countByTag(tag)
    }

    /**
     * 保存图片
     */
    @Auth
    @PostMapping("/save")
    @RestfulPack
    fun save(@CurrentUserId userId: String, url: String, name: String, introduction: String?, privacy: PrivacyState, @RequestParam("tagList") tagList: Set<String>?): DrawVO {
        val draw = Draw(userId, url, name, introduction!!)
        draw.privacy = privacy
        if (tagList != null && tagList.isNotEmpty()) {
            draw.tagList.addAll(tagList.map { Tag(it) })
        }

        qiniuComponent.move(url, baseConfig.qiniuBucket)
        val imageInfo = qiniuComponent.getImageInfo(url, baseConfig.qiniuBucketUrl) ?: throw ProgramException("移除图片出错")
        draw.width = imageInfo.width
        draw.height = imageInfo.height
        return getDrawVO(drawService.save(draw), userId)
    }

    /**
     * 更新
     */
    @Auth
    @PostMapping("/update")
    @RestfulPack
    fun update(@CurrentUserId userId: String, id: String, name: String?, introduction: String?, privacy: PrivacyState, @RequestParam("tagList") tagList: Array<String>?): DrawVO {
        val draw = drawService.get(id)
        draw.userId != userId && throw PermissionException("您无权修改该图片")
        if (name != null && name.isNotEmpty()) draw.name = name
        if (introduction != null && introduction.isNotEmpty()) draw.introduction = introduction
        draw.privacy = privacy
        if (tagList != null && tagList.isNotEmpty()) {
            val tagNameList = draw.tagList.map { it.name }
            val sourceTagList = draw.tagList.toMutableSet()
            for (addTagName in tagList) {
                if (tagNameList.indexOf(addTagName) == -1) {
                    sourceTagList.add(Tag(addTagName))
                }
            }
            for (tag in draw.tagList.toList()) {
                if (tagList.indexOf(tag.name) == -1) {
                    sourceTagList.remove(tag)
                }
            }
            draw.tagList.clear()
            draw.tagList.addAll(sourceTagList)
        }
        return getDrawVO(drawService.save(draw), userId)
    }

    /**
     * 移除图片
     */
    @Auth
    @PostMapping("/remove")
    @RestfulPack
    fun remove(@CurrentUserId userId: String, id: String): Boolean {
        val draw = drawService.get(id)
        if (userId != draw.userId) {
            throw PermissionException("你无权删除该图片")
        }
        qiniuComponent.move(draw.url, baseConfig.qiniuTempBucket, baseConfig.qiniuBucket)
        return drawService.remove(draw.id!!)
    }

    @Auth
    @PostMapping("/batchUpdate")
    @RestfulPack
    fun batchUpdate(@CurrentUserId userId: String, idList: Array<String>, name: String?, introduction: String?, privacy: PrivacyState?, @RequestParam("tagList") tagList: Array<String>?): MutableList<DrawDocument> {
        val drawList = mutableListOf<DrawDocument>()
        for (id in idList) {
            try {
                val draw = drawService.get(id)
                if (userId != draw.userId) {
                    continue
                }
                if (name != null && name.isNotEmpty()) draw.name = name
                if (introduction != null && introduction.isNotEmpty()) draw.introduction = introduction
                privacy?.let { draw.privacy = it }
                if (tagList != null && tagList.isNotEmpty()) {
                    val sourceTagList = draw.tagList.toMutableSet()
                    sourceTagList.addAll(tagList.map { Tag(it) })
                    draw.tagList.clear()
                    draw.tagList.addAll(sourceTagList.distinctBy { it.name })
                }
                drawList.add(drawService.save(draw))
            } catch (e: Exception) {
                println(e.message)
            }
        }
        return drawList
    }

    private fun getPageVO(page: Page<DrawDocument>, userId: String? = null): Page<DrawVO> {
        val drawVOList = page.content.map { getDrawVO(it, userId) }
        return PageImpl(drawVOList, page.pageable, page.totalElements)
    }
}