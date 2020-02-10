package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.exception.SignInException
import com.junjie.secdradata.constant.CollectState
import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdraservice.service.CollectionService
import com.junjie.secdraservice.service.PictureDocumentService
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.core.communal.PictureVOAbstract
import com.junjie.secdraweb.vo.CollectionPictureVO
import com.junjie.secdraweb.vo.FootprintPictureVO
import com.junjie.secdraweb.vo.UserVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.lang.reflect.UndeclaredThrowableException

/**
 * @author fjj
 * 图片收藏的
 */
@RestController
@RequestMapping("collection")
class CollectionController(override val pictureDocumentService: PictureDocumentService,
                           override val collectionService: CollectionService,
                           override val userService: UserService,
                           override val followService: FollowService) : PictureVOAbstract() {
    @Auth
    @PostMapping("focus")
    @RestfulPack
    fun focus(@CurrentUserId userId: String, pictureId: String): CollectState {
        val picture = try {
            pictureDocumentService.get(pictureId)
        } catch (e: NotFoundException) {
            null
        }
        return if (picture == null) {
            //关注了但图片为空立即取消关注
            if (collectionService.exists(userId, pictureId) == CollectState.CONCERNED) {
                collectionService.remove(userId, pictureId)
                CollectState.STRANGE
            } else {
                throw PermissionException("不能收藏已移除图片")
            }
        } else {
            picture.userId == userId && throw ProgramException("不能收藏自己的作品")
            val flag = if (collectionService.exists(userId, pictureId) == CollectState.STRANGE) {
                picture.privacy == PrivacyState.PRIVATE && throw PermissionException("不能收藏私密图片")
                collectionService.save(userId, pictureId)
                CollectState.CONCERNED
            } else {
                collectionService.remove(userId, pictureId)
                CollectState.STRANGE
            }
            pictureDocumentService.saveLikeAmount(picture, collectionService.countByPictureId(pictureId))
            flag
        }
    }

    /**
     * 取消收藏一组
     */
    @Auth
    @PostMapping("unFocus")
    @RestfulPack
    fun unFocus(@CurrentUserId userId: String, @RequestParam("pictureIdList") pictureIdList: Array<String>?): List<String> {
        if (pictureIdList == null || pictureIdList.isEmpty()) {
            throw ProgramException("请选择一张图片")
        }
        val newPictureIdList = mutableListOf<String>()
        for (pictureId in pictureIdList) {
            if (collectionService.exists(userId, pictureId) != CollectState.CONCERNED) {
                continue
            }
            val picture = try {
                pictureDocumentService.get(pictureId)
            } catch (e: NotFoundException) {
                null
            }
            collectionService.remove(userId, pictureId)
            picture?.let {
                pictureDocumentService.saveLikeAmount(it, collectionService.countByPictureId(pictureId))
            }
            newPictureIdList.add(pictureId)
        }
        return newPictureIdList
    }

    /**
     * 获取列表
     */
    @GetMapping("paging")
    @RestfulPack
    fun paging(@CurrentUserId userId: String?, targetId: String?, @PageableDefault(value = 20) pageable: Pageable): Page<CollectionPictureVO> {
        (userId.isNullOrEmpty() && targetId.isNullOrEmpty()) && throw SignInException("请重新登录")
        val page = collectionService.pagingByUserId(targetId ?: userId!!, pageable)
        val collectionPictureVOList = ArrayList<CollectionPictureVO>()
        for (collection in page.content) {
            val collectionPictureVO = try {
                val picture = pictureDocumentService.get(collection.pictureId)
                //图片被隐藏
                if (picture.privacy == PrivacyState.PRIVATE) {
                    picture.url = ""
                }
                CollectionPictureVO(
                        picture,
                        getPictureVO(picture, userId).focus,
                        collection.createDate!!,
                        getUserVO(picture.userId, userId))
            } catch (e: NotFoundException) {
                CollectionPictureVO(collection.pictureId, collectionService.exists(targetId, collection.pictureId), collection.createDate!!)
            } catch (e: PermissionException) {
                CollectionPictureVO(collection.pictureId, collectionService.exists(targetId, collection.pictureId), collection.createDate!!)
            }
            collectionPictureVOList.add(collectionPictureVO)
        }
        return PageImpl(collectionPictureVOList, page.pageable, page.totalElements)
    }


//    private fun <T> test(userId: String?, targetId: String, pictureId: String, clazz: Class<T>): T {
//        val picture = pictureDocumentService.get(pictureId)
//        //图片被隐藏
//        if (picture.privacy == PrivacyState.PRIVATE) {
//            picture.url = ""
//        }
//        val userVO = UserVO(userService.getInfo(picture.userId))
//        userVO.focus = followService.exists(targetId, picture.userId)
//        return clazz.getDeclaredConstructor(PictureDocument::class.java, CollectState::class.java, Date::class.java, UserVO::class.java)
//                .newInstance(picture, if (userId != null && userId == targetId) CollectState.SElF else collectionService.exists(targetId, pictureId), Date(), userVO)
//    }


    @GetMapping("pagingUser")
    @RestfulPack
    fun pagingUser(@CurrentUserId userId: String?, pictureId: String, @PageableDefault(value = 20) pageable: Pageable): Page<UserVO> {
        val page = collectionService.pagingByPictureId(pictureId, pageable)
        val userVOList = page.content.map {
            getUserVO(it.userId, userId)
        }
        return PageImpl(userVOList, page.pageable, page.totalElements)
    }
}