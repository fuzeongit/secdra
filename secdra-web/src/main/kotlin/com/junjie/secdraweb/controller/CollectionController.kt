package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.constant.CollectState
import com.junjie.secdracore.constant.FollowState
import com.junjie.secdracore.constant.PrivacyState
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.service.CollectionService
import com.junjie.secdraservice.service.DrawService
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.vo.DrawVO
import com.junjie.secdraweb.vo.UserVO

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
@RequestMapping("collection")
class CollectionController(private val collectionService: CollectionService, private val drawService: DrawService,
                           private val userService: UserService, private val followService: FollowService) {
    @Auth
    @PostMapping("/focus")
    fun focus(@CurrentUserId userId: String, drawId: String): CollectState {
        val draw = drawService.get(drawId)
        if (draw.userId == userId) {
            throw ProgramException("不能收藏自己的作品")
        }
        val flag = if (collectionService.exists(userId, drawId) == CollectState.STRANGE) {
            if (draw.privacy == PrivacyState.PRIVATE) {
                throw ProgramException("不能收藏私密图片")
            }
            collectionService.save(userId, drawId)
            CollectState.CONCERNED
        } else {
            collectionService.remove(userId, drawId)
            CollectState.STRANGE
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
            if (collectionService.exists(userId, drawId) == CollectState.STRANGE) {
                continue
            }
            try {
                val draw = drawService.get(drawId)
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
    fun paging(@CurrentUserId userId: String, id: String?, @PageableDefault(value = 20) pageable: Pageable): Page<DrawVO> {
        val page = collectionService.paging(
                if (id.isNullOrEmpty()) {
                    userId
                } else {
                    id!!
                }, pageable)
        val drawVOList = ArrayList<DrawVO>()
        for (collection in page.content) {
            var draw: Draw
            try {
                draw = drawService.get(collection.drawId!!)
                if (draw.privacy == PrivacyState.PRIVATE) {
                    draw.url = "";
                }
            } catch (e: Exception) {
                if (e is NotFoundException) {
                    draw = Draw()
                    draw.id = collection.drawId
                    draw.privacy = PrivacyState.PRIVATE
                } else {
                    throw e
                }
            }
            val drawVO = DrawVO()
            BeanUtils.copyProperties(draw, drawVO)

            val userVO = UserVO()
            val user = userService.getInfo(draw.userId!!)
            BeanUtils.copyProperties(user, userVO)
            userVO.focus = followService.exists(userId, draw.userId!!)
            drawVO.focus = if (id.isNullOrEmpty() || id == userId) {
                CollectState.CONCERNED
            } else {
                collectionService.exists(userId, draw.id!!)
            }
            drawVO.user = userVO
            drawVOList.add(drawVO)
        }
        return PageImpl<DrawVO>(drawVOList, page.pageable, page.totalElements)
    }
}