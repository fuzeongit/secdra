package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.model.Result
import com.junjie.secdracore.util.JwtUtil
import com.junjie.secdracore.util.RegexUtil
import com.junjie.secdraservice.constant.Gender
import com.junjie.secdraservice.constant.VerificationCodeOperation
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.FollowService
import com.junjie.secdraservice.service.UserService
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
class UserController(private val userService: UserService, private val baseConfig: BaseConfig,
                     private val qiniuComponent: QiniuComponent, private val redisTemplate: StringRedisTemplate,
                     private val followService: FollowService) {
    /**
     * 发送验证码
     */
    @PostMapping("sendCode")
    fun sendCode(phone: String, verificationCodeOperation: VerificationCodeOperation): Result<String> {
        if (!RegexUtil.checkMobile(phone)) {
            throw ProgramException("请输入正确的手机号码")
        }
        var verificationCode = ""
        while (verificationCode.length < 6) {
            verificationCode += Random().nextInt(10).toString()
        }
        return when (verificationCodeOperation) {
            VerificationCodeOperation.REGISTER -> {
                if (userService.existsByPhone(phone)) {
                    throw ProgramException("手机号码已存在")
                }
//                smsComponent.sendVerificationCode(phone, verificationCode)
                redisTemplate.opsForValue().set(String.format(baseConfig.registerVerificationCodePrefix, phone), verificationCode, baseConfig.verificationCodeTimeout, TimeUnit.MILLISECONDS)
                Result(200, null, verificationCode)
            }
            VerificationCodeOperation.FORGET -> {
                if (!userService.existsByPhone(phone)) {
                    throw ProgramException("手机号码不存在")
                }
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
    fun register(phone: String, password: String, verificationCode: String, response: HttpServletResponse): UserVO {
        if (!RegexUtil.checkMobile(phone)) {
            throw ProgramException("请输入正确的手机号码")
        }
        if (!RegexUtil.checkPassword(password)) {
            throw ProgramException("请输入正确的密码（密码由数字，字母和下划线的6-16位字符组成）")
        }
        val redisCode = redisTemplate.opsForValue()[String.format(baseConfig.registerVerificationCodePrefix, phone)]
        if (verificationCode != redisCode) {
            throw ProgramException("验证码无效")
        }
        //获取系统时间
        val nowMillis = System.currentTimeMillis()
        var user = User()
        user.phone = phone
        user.password = password
        user.rePasswordDate = Date(nowMillis)
        //注册
        user = userService.register(phone, password, Date(nowMillis))
        //清理验证码
        redisTemplate.delete(String.format(baseConfig.registerVerificationCodePrefix, phone))
        //把修改密码时间放到redis
        redisTemplate.opsForValue().set(String.format(baseConfig.updatePasswordTimePrefix, user.id), nowMillis.toString())
        //生成token
        val token = JwtUtil.createJWT(user.id!!, nowMillis, baseConfig.jwtExpiresSecond, baseConfig.jwtSecretString)
        response.setHeader("token", token)
        return UserVO(user)
    }


    /**
     * 修改密码
     */
    @PostMapping("/rePassword")
    fun rePassword(phone: String, password: String, verificationCode: String, response: HttpServletResponse): UserVO {
        if (!RegexUtil.checkMobile(phone)) {
            throw ProgramException("请输入正确的手机号码")
        }
        if (!RegexUtil.checkPassword(password)) {
            throw ProgramException("请输入正确的密码（密码由数字，字母和下划线的6-16位字符组成）")
        }
        val redisCode = redisTemplate.opsForValue()[String.format(baseConfig.forgetVerificationCodePrefix, phone)]
        if (verificationCode != redisCode) {
            throw PermissionException("验证码无效")
        }

        val nowMillis = System.currentTimeMillis()
        val user = userService.rePassword(phone, password, Date(nowMillis))
        redisTemplate.delete(String.format(baseConfig.forgetVerificationCodePrefix, phone))
        redisTemplate.opsForValue().set(String.format(baseConfig.updatePasswordTimePrefix, user.id), nowMillis.toString())
        return UserVO(user)
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    fun login(phone: String, password: String, response: HttpServletResponse): UserVO {
        val user = userService.login(phone, password)
        val nowMillis = System.currentTimeMillis()
        val token = JwtUtil.createJWT(user.id!!, nowMillis, baseConfig.jwtExpiresSecond, baseConfig.jwtSecretString)
        response.setHeader("token", token)
        return UserVO(user)
    }

    @Auth
    @PostMapping("/checkLogin")
    fun checkLogin(): Boolean {
        return true
    }


    /**
     * 获取用户信息
     */
    @Auth
    @GetMapping("/getInfo")
    fun getInfo(@CurrentUserId userId: String, id: String?): UserVO {
        val userVO = UserVO(userService.getInfo(if (id.isNullOrEmpty() || id == userId) {
            userId
        } else {
            id!!
        }))
        userVO.focus = followService.exists(userId, userVO.id!!)
        return userVO
    }


    /**
     * 修改用户信息
     */
    @Auth
    @PostMapping("/update")
    fun update(@CurrentUserId userId: String, name: String?, gender: Gender?, birthday: Date?, introduction: String?, address: String?): UserVO {
        val info = userService.getInfo(userId)
        if (!name.isNullOrEmpty()) info.name = name
        if (gender != null) info.gender = gender
        if (birthday != null) info.birthday = birthday
        if (introduction.isNullOrEmpty()) info.introduction = introduction
        if (address.isNullOrEmpty()) info.address = address
        return UserVO(userService.save(info))
    }


    /**
     * 修改头像
     */
    @Auth
    @PostMapping("/updateHead")
    fun updateHead(@CurrentUserId userId: String, url: String): UserVO {
        val info = userService.getInfo(userId)
        qiniuComponent.move(info.head!!, baseConfig.qiniuTempBucket, baseConfig.qiniuHeadBucket)
        qiniuComponent.move(url, baseConfig.qiniuHeadBucket)
        info.head = url
        return UserVO(userService.save(info))
    }


    /**
     * 修改背景墙
     */
    @Auth
    @PostMapping("/updateBack")
    fun updateBack(@CurrentUserId userId: String, url: String): UserVO {
        val info = userService.getInfo(userId)
        qiniuComponent.move(info.background!!, baseConfig.qiniuTempBucket, baseConfig.qiniuBackBucket)
        qiniuComponent.move(url, baseConfig.qiniuBackBucket)
        info.background = url
        return UserVO(userService.save(info))
    }
}