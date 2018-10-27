package com.junjie.secdraweb.controller

import com.corundumstudio.socketio.SocketIOServer
import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.model.Result
import com.junjie.secdracore.util.JwtUtil
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.base.component.JwtConfig
import com.junjie.secdraweb.base.component.SocketIOEventHandler
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("user")
class UserController(private val userService: IUserService, private val jwtConfig: JwtConfig,private val redisTemplate : StringRedisTemplate,private var socketIoServer: SocketIOServer) {
    /**
     * 注册
     */
    @PostMapping("/register")
    fun register(phone: String, password: String, response: HttpServletResponse): User {
        if (StringUtils.isEmpty(phone)) {
            throw ProgramException("输入手机为空", 404)
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
        redisTemplate.opsForValue().set(String.format(jwtConfig.redisPrefix, user.id),nowMillis.toString())
        val token = JwtUtil.createJWT(user.id!!, nowMillis, jwtConfig.expiresSecond, jwtConfig.base64Secret)
//        val cookie = Cookie("token", token)
//        response.addCookie(cookie)
        response.setHeader("token",token)
        return user
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    fun login(phone: String, password: String, response: HttpServletResponse): User {
        var user:User? = null
        try{
             user = userService.login(phone, password)
        }catch (e:ProgramException){
            if(e.status ==403){
                return this.register(phone,password,response)
            }
        }

        val nowMillis = System.currentTimeMillis()
        val token = JwtUtil.createJWT(user?.id!!, nowMillis, jwtConfig.expiresSecond, jwtConfig.base64Secret)
//        val cookie = Cookie("token", token)
//        response.addCookie(cookie)
        response.setHeader("token",token)
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
    fun rePassword(phone:String ,password:String,response: HttpServletResponse):User{
        val nowMillis = System.currentTimeMillis()
        val user = userService.rePassword(phone,password,Date(nowMillis))
        redisTemplate.opsForValue().set(String.format(jwtConfig.redisPrefix, user.id),nowMillis.toString())
        val token = JwtUtil.createJWT(user.id!!, nowMillis, jwtConfig.expiresSecond, jwtConfig.base64Secret)
        val cookie = Cookie("token", token)
        return user
    }

    /**
     * 获取用户信息
     */
    @Auth
    @GetMapping("/getInfo")
    fun getInfo(@CurrentUserId userId: String): User {
        return userService.getInfo(userId)
    }

    @GetMapping("/get")
    fun getInfo(): String {
        for (clientId in SocketIOEventHandler.listClient) {
            if (socketIoServer.getClient(clientId) == null) continue;

            socketIoServer.getClient(clientId).sendEvent("send", "fuck");
        }
        return "1"
    }
}