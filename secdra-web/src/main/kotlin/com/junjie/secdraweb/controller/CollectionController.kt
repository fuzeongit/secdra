package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.constant.CollectState
import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.service.CollectionService
import com.junjie.secdraservice.service.DrawDocumentService
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.vo.CollectionDrawVO
import com.junjie.secdraweb.vo.UserVO
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
class CollectionController(private val collectionService: CollectionService,
                           private val drawDocumentService: DrawDocumentService,
                           private val userService: UserService, private val followService: FollowService) {
    @Auth
    @PostMapping("/focus")
    @RestfulPack
    fun focus(@CurrentUserId userId: String, drawId: String): CollectState {
        val draw = drawDocumentService.get(drawId)
        draw.userId == userId && throw ProgramException("不能收藏自己的作品")
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
        draw.likeAmount = collectionService.countByDrawId(drawId)
        drawDocumentService.save(draw)
        return flag
    }

    /**
     * 取消收藏一组
     */
    @Auth
    @PostMapping("/unFocus")
    @RestfulPack
    fun unFocus(@CurrentUserId userId: String, @RequestParam("drawIdList") drawIdList: Array<String>?): List<String> {
        if (drawIdList == null || drawIdList.isEmpty()) {
            throw ProgramException("请选择一张图片")
        }
        val newDrawIdList = mutableListOf<String>()
        for (drawId in drawIdList) {
            if (collectionService.exists(userId, drawId) != CollectState.CONCERNED) {
                continue
            }
            try {
                val draw = drawDocumentService.get(drawId)
                collectionService.remove(userId, drawId)
                draw.likeAmount = collectionService.countByDrawId(drawId)
                drawDocumentService.save(draw)
                newDrawIdList.add(draw.id!!)
            } catch (e: Exception) {

            }
        }
        return newDrawIdList
    }

    @Auth
    @GetMapping("/paging")
    @RestfulPack
    fun paging(@CurrentUserId userId: String, id: String?, @PageableDefault(value = 20) pageable: Pageable): Page<CollectionDrawVO> {
        val page = collectionService.paging(if (id.isNullOrEmpty()) userId else id!!, pageable)
        val collectionDrawVOList = ArrayList<CollectionDrawVO>()
        for (collection in page.content) {
            val collectionDrawVO = try {
                val draw = drawDocumentService.get(collection.drawId)
                //图片被隐藏
                if (draw.privacy == PrivacyState.PRIVATE) {
                    //TODO 隐藏图片的默认路径
                    draw.url = ""
                }
                val userVO = UserVO(userService.getInfo(draw.userId))
                userVO.focus = followService.exists(userId, draw.userId)
                CollectionDrawVO(draw, if (id.isNullOrEmpty() || id == userId) CollectState.CONCERNED else collectionService.exists(userId, draw.id!!), userVO)
            } catch (e: Exception) {
                //图片被删除
                if (e is NotFoundException) {
                    CollectionDrawVO(collection.drawId)
                } else {
                    throw e
                }
            }
            collectionDrawVOList.add(collectionDrawVO)
        }
        return PageImpl(collectionDrawVOList, page.pageable, page.totalElements)
    }
}