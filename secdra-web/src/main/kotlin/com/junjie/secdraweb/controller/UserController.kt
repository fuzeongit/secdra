package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.SignInException
import com.junjie.secdradata.constant.Gender
import com.junjie.secdraqiniu.core.component.QiniuConfig
import com.junjie.secdraqiniu.service.BucketService
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.core.communal.UserVOAbstract
import com.junjie.secdraweb.vo.UserVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @author fjj
 * 用户的控制器
 */
@RestController
@RequestMapping("user")
class UserController(private val qiniuConfig: QiniuConfig,
                     private val bucketService: BucketService,
                     override val userService: UserService,
                     override val followService: FollowService) : UserVOAbstract() {

    /**
     * 获取用户信息
     */
    @GetMapping("get")
    @RestfulPack
    fun get(@CurrentUserId userId: String?, id: String?): UserVO {
        (userId.isNullOrEmpty() && id.isNullOrEmpty()) && throw SignInException("请登录")
        return getUserVO(id ?: userId!!, userId)
    }

    /**
     * 修改用户信息
     */
    @Auth
    @PostMapping("update")
    @RestfulPack
    fun update(@CurrentUserId userId: String, name: String?, gender: Gender?, birthday: Date?, introduction: String?, address: String?): UserVO {
        val info = userService.get(userId)
        if (name != null && name.isNotEmpty()) info.name = name
        gender?.let { info.gender = it }
        birthday?.let { info.birthday = it }
        if (introduction != null && introduction.isNotEmpty()) info.introduction = introduction
        if (address != null && address.isNotEmpty()) info.address = address
        return getUserVO(userService.save(info), userId)
    }

    /**
     * 修改头像
     */
    @Auth
    @PostMapping("updateHead")
    @RestfulPack
    fun updateHead(@CurrentUserId userId: String, url: String): UserVO {
        val info = userService.get(userId)
        info.head = movePictureAndSave(info.head, url, qiniuConfig.qiniuHeadBucket)
        return getUserVO(userService.save(info), userId)
    }

    /**
     * 修改背景墙
     */
    @Auth
    @PostMapping("updateBack")
    @RestfulPack
    fun updateBack(@CurrentUserId userId: String, url: String): UserVO {
        val info = userService.get(userId)
        info.background = movePictureAndSave(info.background, url, qiniuConfig.qiniuBackBucket)
        return getUserVO(userService.save(info), userId)
    }

    private fun movePictureAndSave(sourceUrl: String?, url: String, targetBucket: String): String {
        sourceUrl?.let { bucketService.move(it, qiniuConfig.qiniuTempBucket, targetBucket) }
        bucketService.move(url, targetBucket)
        return url
    }
}