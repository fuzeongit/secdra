package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.SignInException
import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdraservice.service.*
import com.junjie.secdraweb.core.communal.PictureVOAbstract
import com.junjie.secdraweb.vo.FootprintPictureVO
import com.junjie.secdraweb.vo.UserVO
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
 * 足迹的控制器
 */
@RestController
@RequestMapping("footprint")
class FootprintController(private val footprintService: FootprintService,
                          override val pictureDocumentService: PictureDocumentService,
                          override val collectionService: CollectionService,
                          override val userService: UserService,
                          override val followService: FollowService) : PictureVOAbstract() {
    @Auth
    @PostMapping("save")
    @RestfulPack
    fun save(@CurrentUserId userId: String, pictureId: String): Long {
        val picture = pictureDocumentService.get(pictureId)
        picture.privacy == PrivacyState.PRIVATE && throw PermissionException("私有图片不能操作")
        try {
            footprintService.update(userId, pictureId)
        } catch (e: NotFoundException) {
            footprintService.save(userId, pictureId)
            // 由于足迹有时效性，所以不能通过表来统计
            pictureDocumentService.saveViewAmount(picture, picture.viewAmount + 1)
        }
        return picture.viewAmount
    }


    @GetMapping("paging")
    @RestfulPack
    fun paging(@CurrentUserId userId: String?, targetId: String?, @PageableDefault(value = 20) pageable: Pageable): Page<FootprintPictureVO> {
        (userId.isNullOrEmpty() && targetId.isNullOrEmpty()) && throw SignInException("请重新登录")
        val page = footprintService.pagingByUserId(targetId ?: userId!!, pageable)
        val footprintPictureVOList = ArrayList<FootprintPictureVO>()
        for (footprint in page.content) {
            val footprintPictureVO = try {
                val picture = pictureDocumentService.get(footprint.pictureId)
                //图片被隐藏
                if (picture.privacy == PrivacyState.PRIVATE) {
                    picture.url = ""
                }
                FootprintPictureVO(
                        picture,
                        getPictureVO(picture, userId).focus,
                        footprint.createDate!!,
                        getUserVO(picture.userId, userId)
                )
            } catch (e: NotFoundException) {
                FootprintPictureVO(footprint.pictureId, collectionService.exists(targetId, footprint.pictureId), footprint.createDate!!)
            }
            footprintPictureVOList.add(footprintPictureVO)
        }
        return PageImpl(footprintPictureVOList, page.pageable, page.totalElements)
    }

    @GetMapping("pagingUser")
    @RestfulPack
    fun pagingUser(@CurrentUserId userId: String?, pictureId: String, @PageableDefault(value = 20) pageable: Pageable): Page<UserVO> {
        val page = footprintService.pagingByPictureId(pictureId, pageable)
        val picture = pictureDocumentService.get(pictureId)
        val userVOList = page.content.map {
            getUserVO(picture.userId, userId)
        }
        return PageImpl(userVOList, page.pageable, page.totalElements)
    }
}