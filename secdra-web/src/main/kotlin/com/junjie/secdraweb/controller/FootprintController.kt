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
            draw.likeAmount = footprintService.countByDrawId(drawId)
            drawDocumentService.save(draw)
        }
        return draw.likeAmount
    }


    @Auth
    @GetMapping("/paging")
    @RestfulPack
    fun paging(@CurrentUserId userId: String, id: String?, @PageableDefault(value = 20) pageable: Pageable): Page<FootprintDrawVO> {
        val page = footprintService.paging(if (id.isNullOrEmpty()) userId else id!!, pageable)
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
                userVO.focus = followService.exists(userId, draw.userId)
                FootprintDrawVO(draw, if (id.isNullOrEmpty() || id == userId) CollectState.CONCERNED else collectionService.exists(userId, footprint.drawId), footprint.createDate!!, userVO)
            } catch (e: NotFoundException) {
                FootprintDrawVO(footprint.drawId, collectionService.exists(userId, footprint.drawId), footprint.createDate!!)
            }
            footprintDrawVOList.add(footprintDrawVO)
        }
        return PageImpl(footprintDrawVOList, page.pageable, page.totalElements)
    }
}