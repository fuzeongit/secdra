package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.constant.CollectState
import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.service.CollectionService
import com.junjie.secdraservice.service.DrawDocumentService
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.base.communal.DrawVOAbstract
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
class CollectionController(override val drawDocumentService: DrawDocumentService,
                           override val collectionService: CollectionService,
                           override val userService: UserService,
                           override val followService: FollowService) : DrawVOAbstract() {
    @Auth
    @PostMapping("/focus")
    @RestfulPack
    fun focus(@CurrentUserId userId: String, drawId: String): CollectState {
        val draw = try {
            drawDocumentService.get(drawId)
        } catch (e: NotFoundException) {
            null
        }
        return if (draw == null) {
            //关注了但图片为空立即取消关注
            if (collectionService.exists(userId, drawId) == CollectState.CONCERNED) {
                collectionService.remove(userId, drawId)
                CollectState.STRANGE
            } else {
                throw PermissionException("不能收藏已移除图片")
            }
        } else {
            draw.userId == userId && throw ProgramException("不能收藏自己的作品")
            val flag = if (collectionService.exists(userId, drawId) == CollectState.STRANGE) {
                draw.privacy == PrivacyState.PRIVATE && throw PermissionException("不能收藏私密图片")
                collectionService.save(userId, drawId)
                CollectState.CONCERNED
            } else {
                collectionService.remove(userId, drawId)
                CollectState.STRANGE
            }
            drawDocumentService.saveLikeAmount(draw, collectionService.countByDrawId(drawId))
            flag
        }
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
            val draw = try {
                drawDocumentService.get(drawId)
            } catch (e: NotFoundException) {
                null
            }
            collectionService.remove(userId, drawId)
            draw?.let {
                drawDocumentService.saveLikeAmount(it, collectionService.countByDrawId(drawId))
            }
            newDrawIdList.add(drawId)
        }
        return newDrawIdList
    }

    /**
     * 获取列表
     */
    @GetMapping("/paging")
    @RestfulPack
    fun paging(@CurrentUserId userId: String?, targetId: String?, @PageableDefault(value = 20) pageable: Pageable): Page<CollectionDrawVO> {
        (userId.isNullOrEmpty() && targetId.isNullOrEmpty()) && throw ProgramException("Are You Kidding Me")
        val page = collectionService.pagingByUserId(targetId ?: userId!!, pageable)
        val collectionDrawVOList = ArrayList<CollectionDrawVO>()
        for (collection in page.content) {
            val collectionDrawVO = try {
                val draw = drawDocumentService.get(collection.drawId)
                //图片被隐藏
                if (draw.privacy == PrivacyState.PRIVATE) {
                    //TODO 隐藏图片的默认路径
                    draw.url = ""
                }
                CollectionDrawVO(
                        draw,
                        getDrawVO(draw, userId).focus,
                        collection.createDate!!,
                        getUserVO(draw.userId, userId))
            } catch (e: NotFoundException) {
                CollectionDrawVO(collection.drawId, collectionService.exists(targetId, collection.drawId), collection.createDate!!)
            }
            collectionDrawVOList.add(collectionDrawVO)
        }
        return PageImpl(collectionDrawVOList, page.pageable, page.totalElements)
    }


//    private fun <T> test(userId: String?, targetId: String, drawId: String, clazz: Class<T>): T {
//        val draw = drawDocumentService.get(drawId)
//        //图片被隐藏
//        if (draw.privacy == PrivacyState.PRIVATE) {
//            draw.url = ""
//        }
//        val userVO = UserVO(userService.getInfo(draw.userId))
//        userVO.focus = followService.exists(targetId, draw.userId)
//        return clazz.getDeclaredConstructor(DrawDocument::class.java, CollectState::class.java, Date::class.java, UserVO::class.java)
//                .newInstance(draw, if (userId != null && userId == targetId) CollectState.SElF else collectionService.exists(targetId, drawId), Date(), userVO)
//    }


    @GetMapping("/pagingUser")
    @RestfulPack
    fun pagingUser(@CurrentUserId userId: String?, drawId: String, @PageableDefault(value = 20) pageable: Pageable): Page<UserVO> {
        val page = collectionService.pagingByDrawId(drawId, pageable)
        val userVOList = page.content.map {
            getUserVO(it.userId, userId)
        }
        return PageImpl(userVOList, page.pageable, page.totalElements)
    }

}