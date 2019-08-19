package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.document.DrawDocument
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.*
import com.junjie.secdraweb.base.component.BaseConfig
import com.junjie.secdraweb.service.QiniuComponent
import com.junjie.secdraweb.vo.DrawVO
import com.junjie.secdraweb.vo.UserVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author fjj
 * 画像的控制器
 */
@RestController
@RequestMapping("draw")
class DrawController(private val drawService: DrawService, private val drawDocumentService: DrawDocumentService, private val userService: UserService,
                     private val collectionService: CollectionService, private val followService: FollowService,
                     private val qiniuComponent: QiniuComponent, private val baseConfig: BaseConfig) {
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
        //由于不会算法，暂时这样写
        return getPageVO(drawDocumentService.paging(pageable, null, false, null, startDate, endDate, null, false), userId)
    }

    /**
     * 随机获取
     */
//    @GetMapping("/listByRecommend")
//    fun listByRecommend(@CurrentUserId userId: String?): ArrayList<DrawVO> {
//        val pageable = PageRequest.of(0, 4)
//        val drawList = drawService.pagingRand(pageable).content
//        val drawVOList = ArrayList<DrawVO>()
//        for (draw in drawList) {
//            drawVOList.add(getVO(draw, userId))
//        }
//        return drawVOList
//    }

    /**
     * 获取图片
     */
    @GetMapping("/get")
    @RestfulPack
    fun get(id: String, @CurrentUserId userId: String?): DrawVO {
        val draw = drawDocumentService.get(id)
        (draw.privacy == PrivacyState.PRIVATE && draw.userId != userId) && throw PermissionException("你没有权限查看该图片")
        return getVO(DrawVO(draw), userId)
    }

    /**
     * 获取tag第一张
     * 会移到ES搜索
     */
    @GetMapping("getFirstByTag")
    @RestfulPack
    fun getFirstByTag(tag: String): DrawVO {
        return getVO(DrawVO(drawDocumentService.getFirstByTag(tag)), null)
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
        if (tagList != null && !tagList.isEmpty()) {
            draw.tagList.addAll(tagList.map { Tag(it) })
        }

        qiniuComponent.move(url, baseConfig.qiniuBucket)
        val imageInfo = qiniuComponent.getImageInfo(url, baseConfig.qiniuBucketUrl) ?: throw ProgramException("移除图片出错")
        draw.width = imageInfo.width
        draw.height = imageInfo.height
        return getVO(DrawVO(drawService.save(draw)),userId)
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
        return getVO(DrawVO(drawService.save(draw)),userId)
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
                if (tagList != null && !tagList.isEmpty()) {
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

    private fun getVO(drawVO: DrawVO, userId: String? = null): DrawVO {
        val userVO = UserVO(userService.getInfo(drawVO.userId))
        userVO.focus = followService.exists(userId, userVO.id)
        userId?.let { drawVO.focus = collectionService.exists(it, drawVO.id) }
        drawVO.user = userVO
        return drawVO
    }

    private fun getPageVO(page: Page<DrawDocument>, userId: String? = null): Page<DrawVO> {
        val drawVOList = page.content.map { getVO(DrawVO(it), userId) }
        return PageImpl<DrawVO>(drawVOList, page.pageable, page.totalElements)
    }
}