package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.service.IDrawService
import com.junjie.secdraservice.service.ICollectionService
import com.junjie.secdraservice.service.IFollowerService
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.vo.DrawVo
import com.junjie.secdraweb.vo.UserVo
import com.qiniu.util.StringUtils
import javassist.NotFoundException
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
        var flag = false
        flag = if (!collectionService.exists(userId, drawId)) {
            collectionService.save(userId, drawId)
            true
        } else {
            collectionService.remove(userId, drawId)
            false
        }
        drawService.update(drawId, null, collectionService.countByDrawId(drawId))
        return flag;
    }

    @Auth
    @GetMapping("/paging")
    fun paging(@CurrentUserId userId: String, id: String?, @PageableDefault(value = 20) pageable: Pageable): Page<DrawVo> {
        val page = if (StringUtils.isNullOrEmpty(id) || id == userId) {
            collectionService.paging(userId, pageable)
        } else {
            collectionService.paging(id!!, pageable)
        }

        val drawVoList = ArrayList<DrawVo>()
        for (collection in page.content) {
            var draw: Draw? = null
            try {
                draw = drawService.get(collection.drawId!!, null)
            } catch (e: Exception) {
                if (e is PermissionException || e is NotFoundException) {
                    draw = Draw()
                    draw.isPrivate = true
                } else {
                    throw e
                }
            }
            val drawVo = DrawVo()
            BeanUtils.copyProperties(draw!!, drawVo)

            val userVo = UserVo()
            if (!StringUtils.isNullOrEmpty(draw.userId)) {
                val user = userService.getInfo(draw.userId!!)
                BeanUtils.copyProperties(user, userVo)
            }
            userVo.isFocus = followerService.exists(userId, draw.userId!!)
            drawVo.isFocus = if (StringUtils.isNullOrEmpty(id) || id == userId) {
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