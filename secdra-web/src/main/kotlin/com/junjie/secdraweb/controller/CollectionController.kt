package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.service.ICollectionService
import com.junjie.secdraservice.service.IDrawService
import com.junjie.secdraservice.service.IFollowerService
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.vo.DrawVo
import com.junjie.secdraweb.vo.UserVo
import javassist.NotFoundException
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

/**
 * @author fjj
 * 图片收藏的
 */
@RestController
@RequestMapping("/collection")
class CollectionController(private val collectionService: ICollectionService, private val drawService: IDrawService,
                           private val userService: IUserService, private val followerService: IFollowerService) {
    @Auth
    @PostMapping("/focus")
    fun focus(@CurrentUserId userId: String, drawId: String): Boolean {
        val draw = drawService.get(drawId, userId)
        if (draw.userId == userId) {
            throw ProgramException("不能收藏自己")
        }
        val flag = if (!collectionService.exists(userId, drawId)) {
            collectionService.save(userId, drawId)
            true
        } else {
            collectionService.remove(userId, drawId)
            false
        }
        drawService.update(drawId, null, collectionService.countByDrawId(drawId))
        return flag;
    }

    /**
     * 取消收藏一组
     */
    @Auth
    @PostMapping("/unFocus")
    fun unFocus(@CurrentUserId userId: String, @RequestParam("drawIdList") drawIdList: Array<String>?): List<String> {
        if (drawIdList == null || drawIdList.isEmpty()) {
            throw ProgramException("请选择一张图片")
        }
        val newDrawIdList = mutableListOf<String>()
        for (drawId in drawIdList) {
            if (!collectionService.exists(userId, drawId)) {
                continue
            }
            try {
                val draw = drawService.get(drawId, userId)
                collectionService.remove(userId, drawId)
                draw.likeAmount = collectionService.countByDrawId(drawId)
                drawService.save(draw)
                newDrawIdList.add(draw.id!!)
            } catch (e: Exception) {

            }
        }
        return newDrawIdList;
    }

    @Auth
    @GetMapping("/paging")
    fun paging(@CurrentUserId userId: String, id: String?, @PageableDefault(value = 20) pageable: Pageable): Page<DrawVo> {
        val page = collectionService.paging(
                if (id.isNullOrEmpty()) {
                    userId
                } else {
                    id!!
                }, pageable)
        val drawVoList = ArrayList<DrawVo>()
        for (collection in page.content) {
            var draw: Draw
            try {
                draw = drawService.get(collection.drawId!!)
            } catch (e: Exception) {
                if (e is PermissionException || e is NotFoundException) {
                    draw = Draw()
                    draw.isPrivate = true
                } else {
                    throw e
                }
            }
            val drawVo = DrawVo()
            BeanUtils.copyProperties(draw, drawVo)

            val userVo = UserVo()
            val user = userService.getInfo(draw.userId!!)
            BeanUtils.copyProperties(user, userVo)
            userVo.isFocus = followerService.exists(userId, draw.userId!!)
            drawVo.isFocus = if (id.isNullOrEmpty() || id == userId) {
                true
            } else {
                collectionService.exists(userId, draw.id!!)
            }
            drawVo.user = userVo
            drawVoList.add(drawVo)
        }
        return PageImpl<DrawVo>(drawVoList, page.pageable, page.totalElements)
    }
}