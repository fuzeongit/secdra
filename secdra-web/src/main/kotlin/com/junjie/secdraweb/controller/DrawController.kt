package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.dao.IDrawDao
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.service.IDrawService
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.vo.DrawVo
import com.junjie.secdraweb.vo.UserVo
import com.qiniu.util.StringUtils
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.util.ArrayList

@RestController
@RequestMapping("/draw")
class DrawController(private val drawService: IDrawService, private val userService: IUserService, val drawDao: IDrawDao) {
    /**
     * 根据标签获取
     */
    @GetMapping("/pagingByTag")
    fun pagingByTag(name: String?, @PageableDefault(value = 20) pageable: Pageable): Page<DrawVo> {
        val page = if (name != null && !StringUtils.isNullOrEmpty(name)) {
            drawService.pagingByTag(pageable, name)
        } else {
            drawService.paging(pageable)
        }
        return getPageVo(page)
    }

    /**
     * 自己获取
     */
    @Auth
    @GetMapping("/pagingBySelf")
    fun pagingBySelf(@CurrentUserId userId: String, @PageableDefault(value = 20) pageable: Pageable): Page<DrawVo> {
        val page = drawService.pagingByUserId(pageable, userId, true)
        return getPageVo(page)
    }

    /**
     * 获取他人
     */
    @GetMapping("/pagingByOthers")
    fun pagingByOthers(userId: String, @PageableDefault(value = 20) pageable: Pageable): Page<DrawVo> {
        val page = drawService.pagingByUserId(pageable, userId, false)
        return getPageVo(page)
    }

    /**
     * 获取图片
     */
    @GetMapping("/get")
    fun get(id: String, @CurrentUserId userId: String?): DrawVo {
        val draw = drawService.get(id, userId)
        return getVo(draw)
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @Auth
    fun update(@CurrentUserId userId: String, drawId: String, desc: String?, isPrivate: Boolean?): DrawVo {
        val draw = drawService.update(userId, drawId, desc!!, isPrivate!!)
        return getVo(draw)
    }

    private fun getVo(draw: Draw): DrawVo {
        val user = userService.getInfo(draw.userId!!)
        val drawVo = DrawVo()
        val userVo = UserVo()
        BeanUtils.copyProperties(draw, drawVo)
        BeanUtils.copyProperties(user, userVo)
        drawVo.user = userVo
        return drawVo
    }

    private fun getPageVo(page: Page<Draw>): Page<DrawVo> {
        val drawVoList = ArrayList<DrawVo>()
        for (draw in page.content) {
            drawVoList.add(getVo(draw))
        }
        return PageImpl<DrawVo>(drawVoList, page.pageable, page.totalElements)
    }

    @GetMapping("/getName")
    fun getName(path: String): Boolean {
        try {
            val file = File(path)
            val fileNameList = file.list()
            for (fileName in fileNameList) {
                if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                    drawService.save("13760029486", fileName, "小可爱")
                }
            }
            return true
        } catch (e: Exception) {
            println(e.message)
            throw e
        }
    }

    @GetMapping("/getFirst")
    fun get(): Draw {
        val id = "402880e566bb5cc60166bb601bfe0373"
        return drawDao.getOne(id)
    }
}