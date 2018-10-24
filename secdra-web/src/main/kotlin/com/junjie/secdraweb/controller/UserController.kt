package com.junjie.secdraweb.controller

import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.util.JwtUtil
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.model.Result
import com.junjie.secdraweb.base.component.JwtConfig
import io.jsonwebtoken.Claims

import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("user")
class UserController(val userService: IUserService, val jwtConfig: JwtConfig) {
    /**
     * 注册
     */
    @PostMapping("/register")
    fun register(phone: String, password: String, response: HttpServletResponse): Result<*> {
        if (StringUtils.isEmpty(phone)) {
            throw ProgramException("输入手机为空", 404)
        }
        val user = userService.register(phone, password)
        val token = JwtUtil.createJWT(user.id!!, jwtConfig.expiresSecond, jwtConfig.base64Secret)
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
        val token = JwtUtil.createJWT(user.id!!, jwtConfig.expiresSecond, jwtConfig.base64Secret)
        val cookie = Cookie("token", token)
        response.addCookie(cookie)
        return user
    }

    @GetMapping("/getInfo")
    @Auth
    fun getInfo(@CurrentUserId userId: String): User {
        return userService.getInfo(userId)
    }

    @GetMapping("/getToken")
    fun getToken(): String {
        return JwtUtil.createJWT("123", jwtConfig.expiresSecond, jwtConfig.base64Secret)
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