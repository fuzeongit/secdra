package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdrasearch.model.DrawDocument
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.CollectionService
import com.junjie.secdraservice.service.DrawService
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.base.component.BaseConfig
import com.junjie.secdraweb.service.QiniuComponent
import com.junjie.secdraweb.vo.DrawVO
import com.junjie.secdraweb.vo.UserVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
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
class DrawController(private val drawService: DrawService, private val userService: UserService,
                     private val collectionService: CollectionService, private val followService: FollowService,
                     private val qiniuComponent: QiniuComponent, private val baseConfig: BaseConfig) {
    /**
     * 根据标签获取
     */
    @GetMapping("/paging")
    fun paging(@CurrentUserId userId: String?, tag: String?, @PageableDefault(value = 20) pageable: Pageable, startDate: Date?, endDate: Date?): Page<DrawVO> {
        val page = drawService.paging(pageable, tag, startDate, endDate)
        return getPageVO(page, userId)
    }

    /**
     * 根据标签获取
     */
    @GetMapping("/pagingIndex")
    fun paging(tag: String, @PageableDefault(value = 20) pageable: Pageable): Page<DrawDocument> {
        return drawService.paging(pageable, tag)
    }

    /**
     * 获取推荐
     */
    @GetMapping("/pagingByRecommend")
    fun pagingByRecommend(@CurrentUserId userId: String?, @PageableDefault(value = 20) pageable: Pageable, startDate: Date?, endDate: Date?): Page<DrawVO> {
        //由于不会算法，暂时这样写
        val page = drawService.pagingRand(pageable)
        return getPageVO(page, userId)
    }

    /**
     * 随机获取
     */
    @GetMapping("/listByRecommend")
    fun listByRecommend(@CurrentUserId userId: String?): ArrayList<DrawVO> {
        val pageable = PageRequest.of(0, 4)
        val drawList = drawService.pagingRand(pageable).content
        val drawVOList = ArrayList<DrawVO>()
        for (draw in drawList) {
            drawVOList.add(getVO(draw, userId))
        }
        return drawVOList
    }

    /**
     * 按userId获取
     */
    @Auth
    @GetMapping("/pagingByUserId")
    fun pagingBySelf(@CurrentUserId userId: String, id: String?, @PageableDefault(value = 20) pageable: Pageable, startDate: Date?, endDate: Date?): Page<DrawVO> {
        val page = drawService.pagingByUserId(pageable, id
                ?: userId, startDate, endDate, id.isNullOrEmpty() || id == userId)
        return getPageVO(page,userId)
    }

    /**
     * 获取他人
     */
    @GetMapping("/pagingByOthers")
    fun pagingByOthers(userId: String, @PageableDefault(value = 20) pageable: Pageable, startDate: Date?, endDate: Date?): Page<DrawVO> {
        val page = drawService.pagingByUserId(pageable, userId, startDate, endDate, false)
        return getPageVO(page, userId)
    }

    /**
     * 获取图片
     */
    @GetMapping("/get")
    fun get(id: String, @CurrentUserId userId: String?): DrawVO {
        val draw = drawService.get(id)
        if (draw.isPrivate && draw.userId != userId) {
            draw.url = ""
        }
        return getVO(draw, userId)
    }

    /**
     * 获取tag第一张
     */
    @GetMapping("getFirstByTag")
    fun getFirstByTag(tag: String): DrawVO {
        val draw = drawService.getFirstByTag(tag)
        return getVO(draw, null)
    }

    /**
     * 按tag统计图片
     */
    @GetMapping("countByTag")
    fun countByTag(tag: String): Long {
        return drawService.countByTag(tag)
    }

    /**
     * 保存图片
     */
    @Auth
    @PostMapping("/save")
    fun save(@CurrentUserId userId: String, url: String, name: String, introduction: String?, isPrivate: Boolean, @RequestParam("tagList") tagList: Array<String>?): DrawVO {
        val draw = Draw()
        draw.url = url
        draw.userId = userId
        draw.name = name
        draw.introduction = introduction
        draw.isPrivate = isPrivate
        if (tagList != null && !tagList.isEmpty()) {
            for (tagName in tagList) {
                val tag = Tag()
                tag.name = tagName
                draw.tagList.add(tag)
            }
        }

        qiniuComponent.move(url, baseConfig.qiniuBucket)
        val imageInfo = qiniuComponent.getImageInfo(url, baseConfig.qiniuBucketUrl)
        draw.width = imageInfo!!.width
        draw.height = imageInfo.height
        return getVO(drawService.save(draw))
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @Auth
    fun update(@CurrentUserId userId: String, id: String, name: String?, introduction: String?, isPrivate: Boolean, @RequestParam("tagList") tagList: Array<String>?): DrawVO {
        val draw = drawService.get(id)
        if (draw.userId != userId) {
            PermissionException("您无权修改该图片")
        }
        if (!name.isNullOrEmpty()) {
            draw.name = name
        }
        if (!introduction.isNullOrEmpty()) {
            draw.introduction = introduction
        }
        draw.isPrivate = isPrivate

        if (!tagList!!.isEmpty()) {
            val tagNameList = draw.tagList.map { it.name }
            for (addTagName in tagList) {
                if (tagNameList.indexOf(addTagName) == -1) {
                    val tag = Tag()
                    tag.name = addTagName
                    draw.tagList.add(tag)
                }
            }
            for (tag in draw.tagList.toList()){
                if (tagList.indexOf(tag.name) == -1) {
                    draw.tagList.remove(tag)
                }
            }
        }
        return getVO(drawService.save(draw))
    }


    @PostMapping("/batchUpdate")
    @Auth
    fun batchUpdate(@CurrentUserId userId: String, idList: Array<String>, name: String?, introduction: String?, isPrivate: Boolean?, @RequestParam("tagList") tagList: Array<String>?): MutableList<Draw> {
        val drawList = mutableListOf<Draw>()
        for (id in idList) {
            try {
                val draw = drawService.get(id)
                if (userId != draw.userId) {
                    continue
                }
                if (!name.isNullOrEmpty()) {
                    draw.name = name
                }
                if (!introduction.isNullOrEmpty()) {
                    draw.introduction = introduction
                }
                if (isPrivate != null) {
                    draw.isPrivate = isPrivate
                }
                if (!tagList!!.isEmpty()) {
                    val tagNameList = draw.tagList.map { it.name }
                    for (addTagName in tagList) {
                        if (tagNameList.indexOf(addTagName) == -1) {
                            val tag = Tag()
                            tag.name = addTagName
                            draw.tagList.add(tag)
                        }
                    }
                }
                drawList.add(drawService.save(draw))
            } catch (e: Exception) {
                println(e.message)
            }
        }
        return drawList
    }

    private fun getVO(draw: Draw, userId: String? = null): DrawVO {
        val drawVO = DrawVO(draw)
        val userVO = UserVO(userService.getInfo(draw.userId!!))
        userVO.isFocus = followService.exists(userId, userVO.id!!)
        if (!userId.isNullOrEmpty()) {
            drawVO.isFocus = collectionService.exists(userId!!, draw.id!!)
        }
        drawVO.user = userVO
        return drawVO
    }

    private fun getPageVO(page: Page<Draw>, userId: String? = null): Page<DrawVO> {
        val drawVOList = ArrayList<DrawVO>()
        for (draw in page.content) {
            drawVOList.add(getVO(draw, userId))
        }
        return PageImpl<DrawVO>(drawVOList, page.pageable, page.totalElements)
    }
}