package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.dao.IDrawDao
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.IDrawService
import com.junjie.secdraservice.service.ICollectionService
import com.junjie.secdraservice.service.IFollowerService
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.base.component.BaseConfig
import com.junjie.secdraweb.base.component.QiniuComponent
import com.junjie.secdraweb.vo.DrawVo
import com.junjie.secdraweb.vo.UserVo
import com.qiniu.util.StringUtils
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author fjj
 * 画像的控制器
 */
@RestController
@RequestMapping("/draw")
class DrawController(private val drawService: IDrawService, private val userService: IUserService,
                     private val collectionService: ICollectionService, private val followerService: IFollowerService,
                     private val qiniuComponent: QiniuComponent, private val baseConfig: BaseConfig,
                     val drawDao: IDrawDao) {
    /**
     * 根据标签获取
     */
    @GetMapping("/paging")
    fun paging(@CurrentUserId userId: String?, tag: String?, @PageableDefault(value = 20) pageable: Pageable, startDate: Date?, endDate: Date?): Page<DrawVo> {
        val page = drawService.paging(pageable, tag, startDate, endDate)
        return getPageVo(page, userId)
    }

    /**
     * 获取推荐
     */
    @GetMapping("/pagingByRecommend")
    fun pagingByRecommend(@CurrentUserId userId: String?, @PageableDefault(value = 20) pageable: Pageable, startDate: Date?, endDate: Date?): Page<DrawVo> {
        //由于不会算法，暂时这样写
        val page = drawService.pagingRand(pageable)
        return getPageVo(page, userId)
    }

    /**
     * 随机获取
     */
    @GetMapping("/listByRecommend")
    fun listByRecommend(@CurrentUserId userId: String?): ArrayList<DrawVo> {
        val pageable = PageRequest.of(0, 4)
        val drawList = drawService.pagingRand(pageable).content
        val drawVoList = ArrayList<DrawVo>()
        for (draw in drawList) {
            drawVoList.add(getVo(draw, userId))
        }
        return drawVoList
    }

    /**
     * 按userId获取
     */
    @Auth
    @GetMapping("/pagingByUserId")
    fun pagingBySelf(@CurrentUserId userId: String, id: String?, @PageableDefault(value = 20) pageable: Pageable, startDate: Date?, endDate: Date?): Page<DrawVo> {
        val page = if (StringUtils.isNullOrEmpty(id) || id == userId) {
            drawService.pagingByUserId(pageable, userId, startDate, endDate, true)
        } else {
            drawService.pagingByUserId(pageable, id!!, startDate, endDate, false)
        }
        return getPageVo(page)
    }

    /**
     * 获取他人
     */
    @GetMapping("/pagingByOthers")
    fun pagingByOthers(userId: String, @PageableDefault(value = 20) pageable: Pageable, startDate: Date?, endDate: Date?): Page<DrawVo> {
        val page = drawService.pagingByUserId(pageable, userId, startDate, endDate, false)
        return getPageVo(page, userId)
    }

    /**
     * 获取图片
     */
    @GetMapping("/get")
    fun get(id: String, @CurrentUserId userId: String?): DrawVo {
        val draw = drawService.get(id, userId)

        return getVo(draw, userId)
    }

    /**
     * 获取tag第一张
     */
    @GetMapping("getFirstByTag")
    fun getFirstByTag(tag: String): DrawVo {
        val draw = drawService.getFirstByTag(tag)
        return getVo(draw, null)
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
    fun save(@CurrentUserId userId: String, url: String, name: String, desc: String?, isPrivate: Boolean, tagList: Array<String>?): DrawVo {
        val draw = Draw()
        draw.url = url
        draw.userId = userId
        draw.name = name
        draw.introduction = desc
        draw.isPrivate = isPrivate
        if (tagList != null && !tagList.isEmpty()) {
            for (tagName in tagList) {
                val tag = Tag()
                tag.name = tagName
                draw.tagList.add(tag)
            }
        }

//        qiniuComponent.move(url, baseConfig.qiniuBucket)
//        val imageInfo = qiniuComponent.getImageInfo(url, baseConfig.qiniuBucketUrl)
//        draw.width = imageInfo!!.width
//        draw.height = imageInfo.height
        return getVo(drawService.save(draw))
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @Auth
    fun update(@CurrentUserId userId: String, id: String, desc: String?, isPrivate: Boolean?): DrawVo {
        val draw = drawService.update(userId, id, desc!!, isPrivate!!)
        return getVo(draw)
    }


    @PostMapping("/batchUpdate")
    @Auth
    fun batchUpdate(@CurrentUserId userId: String, idList: Array<String>, name: String?, introduction: String?, isPrivate: Boolean?, tagList: Array<String>?): Boolean {
        for (id in idList) {
            try {
                val draw = drawService.get(id, userId)
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
                if (!tagList!!.isEmpty()){
                    val tagNameList = draw.tagList.map { it.name }
                    for (addTagName in tagList){
                        if(tagNameList.indexOf(addTagName) == -1){
                            val tag = Tag()
                            tag.name = addTagName
                            draw.tagList.add(tag)
                        }
                    }
                }
                drawService.save(draw)
            } catch (e: Exception) {
                println(e.message)
            }
        }
        return true
    }

    private fun getVo(draw: Draw, userId: String? = null): DrawVo {
        val user = userService.getInfo(draw.userId!!)
        val drawVo = DrawVo()
        val userVo = UserVo()
        BeanUtils.copyProperties(draw, drawVo)
        BeanUtils.copyProperties(user, userVo)
        userVo.isFocus = followerService.exists(userId, user.id!!)
        if (!StringUtils.isNullOrEmpty(userId)) {
            drawVo.isFocus = collectionService.exists(userId!!, draw.id!!)
        }
        drawVo.user = userVo
        return drawVo
    }

    private fun getPageVo(page: Page<Draw>, userId: String? = null): Page<DrawVo> {
        val drawVoList = ArrayList<DrawVo>()
        for (draw in page.content) {
            drawVoList.add(getVo(draw, userId))
        }
        return PageImpl<DrawVo>(drawVoList, page.pageable, page.totalElements)
    }
}