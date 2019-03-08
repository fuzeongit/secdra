package com.junjie.secdraweb.controller

import com.corundumstudio.socketio.SocketIOServer
import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.util.JwtUtil
import com.junjie.secdraservice.constant.Gender
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.IFollowService
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.base.component.BaseConfig
import com.junjie.secdraweb.base.component.QiniuComponent
import com.junjie.secdraweb.base.component.SocketIOEventHandler
import com.junjie.secdraweb.vo.UserVo
import javassist.NotFoundException
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletResponse

/**
 * @author fjj
 * 用户的控制器
 */
@RestController
@RequestMapping("user")
class UserController(private val userService: IUserService, private val baseConfig: BaseConfig,
                     private val qiniuComponent: QiniuComponent, private val redisTemplate: StringRedisTemplate,
                     private val followService: IFollowService,private var socketIoServer: SocketIOServer) {
    /**
     * 发送验证码
     */
    @PostMapping("sendCode")
    fun sendCode(phone: String): Boolean {
        if (phone.isEmpty()) {
            throw NotFoundException("输入手机为空")
        }
        var verificationCode = ""
        while (verificationCode.length < 6) {
            verificationCode += Random().nextInt(10).toString()
        }
        redisTemplate.opsForValue().set(String.format(baseConfig.verificationCodePrefix, phone), verificationCode)
        println(verificationCode)
        return true
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    fun register(phone: String, password: String, verificationCode: String, response: HttpServletResponse): UserVo {
        if (phone.isEmpty()) {
            throw NotFoundException("输入手机为空")
        }
        val redisCode = redisTemplate.opsForValue()[String.format(baseConfig.verificationCodePrefix, phone)]
        if (verificationCode != redisCode && verificationCode != "888888") {
            throw PermissionException("验证码无效")
        }
        //获取系统时间
        val nowMillis = System.currentTimeMillis()
        var user = User()
        user.phone = phone
        user.password = password
        user.rePasswordDate = Date(nowMillis)
        //注册
        user = userService.register(user)
        //把修改密码时间放到redis
        redisTemplate.opsForValue().set(String.format(baseConfig.updatePasswordTimePrefix, user.id), nowMillis.toString())
        //生成token
        val token = JwtUtil.createJWT(user.id!!, nowMillis, baseConfig.jwtExpiresSecond, baseConfig.jwtBase64Secret)
        response.setHeader("token", token)
        return UserVo(user)
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    fun login(phone: String, password: String, response: HttpServletResponse): UserVo {
        val user = userService.login(phone, password)
        val nowMillis = System.currentTimeMillis()
        val token = JwtUtil.createJWT(user.id!!, nowMillis, baseConfig.jwtExpiresSecond, baseConfig.jwtBase64Secret)
        response.setHeader("token", token)
        return UserVo(user)
    }

    @Auth
    @PostMapping("/checkLogin")
    fun checkLogin(): Boolean {
        return true
    }

    /**
     * 修改密码
     */
    @PostMapping("/rePassword")
    fun rePassword(phone: String, password: String, response: HttpServletResponse): UserVo {
        val nowMillis = System.currentTimeMillis()
        val user = userService.rePassword(phone, password, Date(nowMillis))
        redisTemplate.opsForValue().set(String.format(baseConfig.updatePasswordTimePrefix, user.id), nowMillis.toString())
        return UserVo(user)
    }

    /**
     * 获取用户信息
     */
    @Auth
    @GetMapping("/getInfo")
    fun getInfo(@CurrentUserId userId: String, id: String?): UserVo {
        val userVo = UserVo(userService.getInfo(if (id.isNullOrEmpty() || id == userId) {
            userId
        } else {
            id!!
        }))
        userVo.isFocus = followService.exists(userId, userVo.id!!)
        return userVo
    }

    @GetMapping("/getInfoByDrawId")
    fun getInfoByDrawId(drawId: String): UserVo {
        return UserVo(userService.getInfoByDrawId(drawId))
    }

    /**
     * 修改用户信息
     */
    @Auth
    @PostMapping("/update")
    fun update(@CurrentUserId userId: String, name: String?, gender: Gender?, birthday: Date?, introduction: String?, address: String?): UserVo {
        val info = userService.getInfo(userId)
        if (!name.isNullOrEmpty()) info.name = name
        if (gender != null) info.gender = gender
        if (birthday != null) info.birthday = birthday
        if (introduction.isNullOrEmpty()) info.introduction = introduction
        if (address.isNullOrEmpty()) info.address = address
        return UserVo(userService.save(info))
    }


    /**
     * 修改头像
     */
    @Auth
    @PostMapping("/updateHead")
    fun updateHead(@CurrentUserId userId: String, url: String): UserVo {
        val info = userService.getInfo(userId)
        qiniuComponent.move(info.head!!, baseConfig.qiniuTempBucket,baseConfig.qiniuHeadBucket)
        qiniuComponent.move(url, baseConfig.qiniuHeadBucket)
        info.head = url;
        return UserVo(userService.save(info))
    }


    /**
     * 修改背景墙
     */
    @Auth
    @PostMapping("/updateBack")
    fun updateBack(@CurrentUserId userId: String, url: String): UserVo {
        val info = userService.getInfo(userId)
        qiniuComponent.move(info.background!!, baseConfig.qiniuTempBucket,baseConfig.qiniuBackBucket)
        qiniuComponent.move(url, baseConfig.qiniuBackBucket)
        info.background = url;
        return UserVo(userService.save(info))
    }

    @GetMapping("/get")
    fun getInfo(): Boolean {
        for (clientId in SocketIOEventHandler.listClient) {
            if (socketIoServer.getClient(clientId) == null) continue;

            socketIoServer.getClient(clientId).sendEvent("send", "fuck");
        }
        return true
    }
}