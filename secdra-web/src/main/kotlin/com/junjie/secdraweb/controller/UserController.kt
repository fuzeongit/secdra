package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.model.Result
import com.junjie.secdracore.util.JwtUtil
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.base.component.JwtConfig
import io.jsonwebtoken.Claims
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("user")
class UserController(private val userService: IUserService, private val jwtConfig: JwtConfig,private val redisTemplate : StringRedisTemplate) {
    /**
     * 注册
     */
    @PostMapping("/register")
    fun register(phone: String, password: String, response: HttpServletResponse): Result<*> {
        if (StringUtils.isEmpty(phone)) {
            throw ProgramException("输入手机为空", 404)
        }
        val nowMillis = System.currentTimeMillis()
        var user = User()
        user.phone = phone
        user.password = password
        user.updatePasswordDate = Date(nowMillis)

        user = userService.register(user)
        val key = String.format(jwtConfig.redisPrefix, user.id)
        redisTemplate.opsForValue().set(key,nowMillis.toString())
        val token = JwtUtil.createJWT(user.id!!, nowMillis, jwtConfig.expiresSecond, jwtConfig.base64Secret)
        val cookie = Cookie("token", token)
        response.addCookie(cookie)
        return Result(user)
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    fun login(phone: String, password: String, response: HttpServletResponse): User {
        val user = userService.login(phone, password)
        val nowMillis = System.currentTimeMillis()
        val token = JwtUtil.createJWT(user.id!!, nowMillis, jwtConfig.expiresSecond, jwtConfig.base64Secret)
        val cookie = Cookie("token", token)
        response.addCookie(cookie)
        return user
    }

    @PostMapping("/getInfo")
    @Auth
    fun getInfo(@CurrentUserId userId: String): User {
        return userService.getInfo(userId)
    }

    @GetMapping("/getToken")
    fun getToken(): String {
        val nowMillis = System.currentTimeMillis()
        return JwtUtil.createJWT("123", nowMillis, jwtConfig.expiresSecond, jwtConfig.base64Secret)
    }

    @GetMapping("postToken")
    fun postToken(token: String): Claims {
        return JwtUtil.parseJWT(token, jwtConfig.base64Secret)
    }

    @GetMapping("test")
    fun test(): String {
        return jwtConfig.base64Secret
    }
}