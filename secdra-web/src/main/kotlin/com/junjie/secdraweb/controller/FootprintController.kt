package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdraservice.constant.CollectState
import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.service.*
import com.junjie.secdraweb.vo.CollectionDrawVO
import com.junjie.secdraweb.vo.FootprintDrawVO
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
                          private val drawDocumentService: DrawDocumentService,
                          private val userService: UserService,
                          private val collectionService: CollectionService,
                          private val followService: FollowService) {
    @Auth
    @PostMapping("/save")
    @RestfulPack
    fun save(@CurrentUserId userId: String, drawId: String): Long {
        val draw = drawDocumentService.get(drawId)
        draw.privacy == PrivacyState.PRIVATE && throw PermissionException("私有图片不能操作")
        try {
            footprintService.update(userId, drawId)
        } catch (e: NotFoundException) {
            footprintService.save(userId, drawId)
            // 由于足迹有时效性，所以不能通过表来统计
            drawDocumentService.saveViewAmount(draw, draw.viewAmount + 1)
        }
        return draw.viewAmount
    }


    @GetMapping("/paging")
    @RestfulPack
    fun paging(@CurrentUserId userId: String?, targetId: String, @PageableDefault(value = 20) pageable: Pageable): Page<FootprintDrawVO> {
        val page = footprintService.pagingByUserId(targetId, pageable)
        val footprintDrawVOList = ArrayList<FootprintDrawVO>()
        for (footprint in page.content) {
            val footprintDrawVO = try {
                val draw = drawDocumentService.get(footprint.drawId)
                //图片被隐藏
                if (draw.privacy == PrivacyState.PRIVATE) {
                    //TODO 隐藏图片的默认路径
                    draw.url = ""
                }
                val userVO = UserVO(userService.getInfo(draw.userId))
                userVO.focus = followService.exists(targetId, draw.userId)
                FootprintDrawVO(draw, if (userId != null && userId == footprint.userId) CollectState.SElF else collectionService.exists(targetId, footprint.drawId), footprint.createDate!!, userVO)
            } catch (e: NotFoundException) {
                FootprintDrawVO(footprint.drawId, collectionService.exists(targetId, footprint.drawId), footprint.createDate!!)
            }
            footprintDrawVOList.add(footprintDrawVO)
        }
        return PageImpl(footprintDrawVOList, page.pageable, page.totalElements)
    }

    @GetMapping("/pagingUser")
    @RestfulPack
    fun pagingUser(@CurrentUserId userId: String?, drawId: String, @PageableDefault(value = 20) pageable: Pageable): Page<UserVO> {
        val page = footprintService.pagingByDrawId(drawId, pageable)
        val draw = drawDocumentService.get(drawId)
        val userVOList = page.content.map {
            val userVO = UserVO(userService.getInfo(draw.userId))
            userVO.focus = followService.exists(userId, it.userId)
            userVO
        }
        return PageImpl(userVOList, page.pageable, page.totalElements)
    }
}