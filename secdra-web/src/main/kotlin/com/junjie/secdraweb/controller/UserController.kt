package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.model.Result
import com.junjie.secdracore.util.JwtUtil
import com.junjie.secdracore.util.RegexUtil
import com.junjie.secdraservice.constant.Gender
import com.junjie.secdraservice.constant.VerificationCodeOperation
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.base.communal.UserVOAbstract
import com.junjie.secdraweb.base.component.BaseConfig
import com.junjie.secdraweb.service.QiniuComponent
import com.junjie.secdraweb.vo.UserVO
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletResponse

/**
 * @author fjj
 * 用户的控制器
 */
@RestController
@RequestMapping("user")
class UserController(private val baseConfig: BaseConfig,
                     private val qiniuComponent: QiniuComponent,
                     private val redisTemplate: StringRedisTemplate,
                     override val userService: UserService,
                     override val followService: FollowService) : UserVOAbstract() {
    /**
     * 发送验证码
     */
    @PostMapping("sendCode")
    @RestfulPack
    fun sendCode(phone: String, verificationCodeOperation: VerificationCodeOperation): Result<String> {
        !RegexUtil.checkMobile(phone) && throw ProgramException("请输入正确的手机号码")
        var verificationCode = ""
        while (verificationCode.length < 6) {
            verificationCode += Random().nextInt(10).toString()
        }
        return when (verificationCodeOperation) {
            VerificationCodeOperation.REGISTER -> {
                userService.existsByPhone(phone) && throw ProgramException("手机号码已存在")
//                smsComponent.sendVerificationCode(phone, verificationCode)
                redisTemplate.opsForValue().set(String.format(baseConfig.registerVerificationCodePrefix, phone), verificationCode, baseConfig.verificationCodeTimeout, TimeUnit.MILLISECONDS)
                Result(200, null, verificationCode)
            }
            VerificationCodeOperation.FORGET -> {
                !userService.existsByPhone(phone) && throw ProgramException("手机号码不存在")
//                smsComponent.sendVerificationCode(phone, verificationCode)
                redisTemplate.opsForValue().set(String.format(baseConfig.forgetVerificationCodePrefix, phone), verificationCode, baseConfig.verificationCodeTimeout, TimeUnit.MILLISECONDS)
                Result(200, null, verificationCode)
            }
            else -> {
                Result(200, null, "888888")
            }
        }
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    @RestfulPack
    fun register(phone: String, password: String, verificationCode: String, response: HttpServletResponse): UserVO {
        !RegexUtil.checkMobile(phone) && throw ProgramException("请输入正确的手机号码")
        !RegexUtil.checkPassword(password) && throw ProgramException("请输入正确的密码（密码由数字，字母和下划线的6-16位字符组成）")
        val redisCode = redisTemplate.opsForValue()[String.format(baseConfig.registerVerificationCodePrefix, phone)]
        verificationCode != redisCode && throw ProgramException("验证码无效")
        //获取系统时间
        val nowMillis = System.currentTimeMillis()
        val user = userService.register(phone, password, Date(nowMillis))
        //清理验证码
        redisTemplate.delete(String.format(baseConfig.registerVerificationCodePrefix, phone))
        //把修改密码时间放到redis
        redisTemplate.opsForValue().set(String.format(baseConfig.updatePasswordTimePrefix, user.id), nowMillis.toString())
        //生成token
        val token = JwtUtil.createJWT(user.id!!, nowMillis, baseConfig.jwtExpiresSecond, baseConfig.jwtSecretString)
        response.setHeader("token", token)
        return getUserVO(user, user.id)
    }


    /**
     * 修改密码
     */
    @PostMapping("/rePassword")
    @RestfulPack
    fun rePassword(phone: String, password: String, verificationCode: String, response: HttpServletResponse): UserVO {
        !RegexUtil.checkMobile(phone) && throw ProgramException("请输入正确的手机号码")
        !RegexUtil.checkPassword(password) && throw ProgramException("请输入正确的密码（密码由数字，字母和下划线的6-16位字符组成）")
        val redisCode = redisTemplate.opsForValue()[String.format(baseConfig.forgetVerificationCodePrefix, phone)]
        verificationCode != redisCode && throw PermissionException("验证码无效")
        val nowMillis = System.currentTimeMillis()
        val user = userService.rePassword(phone, password, Date(nowMillis))
        redisTemplate.delete(String.format(baseConfig.forgetVerificationCodePrefix, phone))
        redisTemplate.opsForValue().set(String.format(baseConfig.updatePasswordTimePrefix, user.id), nowMillis.toString())
        return getUserVO(user, user.id)
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    @RestfulPack
    fun login(phone: String, password: String, response: HttpServletResponse): UserVO {
        val user = userService.login(phone, password)
        val nowMillis = System.currentTimeMillis()
        val token = JwtUtil.createJWT(user.id!!, nowMillis, baseConfig.jwtExpiresSecond, baseConfig.jwtSecretString)
        response.setHeader("token", token)
        return getUserVO(user, user.id)
    }

    @Auth
    @PostMapping("/checkLogin")
    @RestfulPack
    fun checkLogin(): Boolean {
        return true
    }


    /**
     * 获取用户信息
     */
    @GetMapping("/getInfo")
    @RestfulPack
    fun getInfo(@CurrentUserId userId: String?, id: String?): UserVO {
        (userId.isNullOrEmpty() && id.isNullOrEmpty()) && throw ProgramException("Are You Kidding Me")
        return getUserVO(id ?: userId!!, userId)
    }


    /**
     * 修改用户信息
     */
    @Auth
    @PostMapping("/update")
    @RestfulPack
    fun update(@CurrentUserId userId: String, name: String?, gender: Gender?, birthday: Date?, introduction: String?, address: String?): UserVO {
        val info = userService.getInfo(userId)
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
    @PostMapping("/updateHead")
    @RestfulPack
    fun updateHead(@CurrentUserId userId: String, url: String): UserVO {
        val info = userService.getInfo(userId)
        info.head = movePictureAndSave(info.head, url, baseConfig.qiniuHeadBucket)
        return getUserVO(userService.save(info), userId)
    }


    /**
     * 修改背景墙
     */
    @Auth
    @PostMapping("/updateBack")
    @RestfulPack
    fun updateBack(@CurrentUserId userId: String, url: String): UserVO {
        val info = userService.getInfo(userId)
        info.background = movePictureAndSave(info.background, url, baseConfig.qiniuBackBucket)
        return getUserVO(userService.save(info), userId)
    }

    private fun movePictureAndSave(sourceUrl: String?, url: String, targetBucket: String): String {
        sourceUrl?.let { qiniuComponent.move(it, baseConfig.qiniuTempBucket, targetBucket) }
        qiniuComponent.move(url, targetBucket)
        return url
    }
}