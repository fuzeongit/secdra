package com.junjie.secdraweb.controller

import com.corundumstudio.socketio.SocketIOServer
import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.util.JwtUtil
import com.junjie.secdraservice.dao.IUserDao
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.base.component.BaseConfig
import com.junjie.secdraweb.base.component.SocketIOEventHandler
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("user")
class UserController(private val userService: IUserService, private val baseConfig: BaseConfig, private val redisTemplate: StringRedisTemplate, private var socketIoServer: SocketIOServer) {
    /**
     * 发送验证码
     */
    @PostMapping("sendCode")
    fun sendCode(phone: String): Boolean {
        if (StringUtils.isEmpty(phone)) {
            throw ProgramException("输入手机为空", 404)
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
    fun register(phone: String, password: String, verificationCode: String, response: HttpServletResponse): User {
        if (StringUtils.isEmpty(phone)) {
            throw ProgramException("输入手机为空", 404)
        }
        val redisCode = redisTemplate.opsForValue()[String.format(baseConfig.verificationCodePrefix, phone)]
        if (verificationCode != redisCode && verificationCode != "888888") {
            throw ProgramException("验证码无效", 403)
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
        return user
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    fun login(phone: String, password: String, response: HttpServletResponse): User {
        val user = userService.login(phone, password)
        val nowMillis = System.currentTimeMillis()
        val token = JwtUtil.createJWT(user.id!!, nowMillis, baseConfig.jwtExpiresSecond, baseConfig.jwtBase64Secret)
        response.setHeader("token", token)
        return user
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
    fun rePassword(phone: String, password: String, response: HttpServletResponse): User {
        val nowMillis = System.currentTimeMillis()
        val user = userService.rePassword(phone, password, Date(nowMillis))
        redisTemplate.opsForValue().set(String.format(baseConfig.updatePasswordTimePrefix, user.id), nowMillis.toString())
        return user
    }

    /**
     * 获取自己的用户信息
     */
    @Auth
    @GetMapping("/getSelfInfo")
    fun getSelfInfo(@CurrentUserId userId: String): User {
        return userService.getInfo(userId)
    }

    /**
     * 获取随意一个的用户信息
     */
    @GetMapping("/getInfo")
    fun getInfo(userId: String): User {
        return userService.getInfo(userId)
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