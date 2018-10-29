package com.junjie.secdraweb.base.interceptor

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.util.CookieUtil
import com.junjie.secdracore.util.DateUtil
import com.junjie.secdracore.util.JwtUtil
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.base.component.JwtConfig
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.lang.Nullable
import org.springframework.util.StringUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthInterceptor(private val jwtConfig: JwtConfig, private val redisTemplate: StringRedisTemplate, val userService: IUserService) : HandlerInterceptor {
    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            val hm = handler as HandlerMethod?
            //程序运行的bean
            hm!!.beanType
            //运行的方法
            val m = hm.method

            //如果存在auth则需验证进入
            if (m.isAnnotationPresent(Auth::class.java)) {
                try {
                    val cookieMap = CookieUtil.readCookieMap(request)
                    val token = cookieMap["token"]
                    val claims = JwtUtil.parseJWT(token!!.value, jwtConfig.base64Secret)
                    val userId = claims["userId"]
                    //过期时间
                    val exp = Date(claims["exp"]?.toString()?.toLong()!! * 1000)
                    //生成时间
                    val nbf = Date(claims["nbf"]?.toString()?.toLong()!! * 1000)
                    //最后更改密码时间
                    val rePasswordDateStr = redisTemplate.opsForValue()[String.format(jwtConfig.redisPrefix, userId)]
                    val rePasswordDate: Date?
                    //缓存穿透
                    rePasswordDate = if (StringUtils.isEmpty(rePasswordDateStr)) {
                        val info = userService.getInfo(userId.toString())
                        //最后更改密码时间写入redis
                        redisTemplate.opsForValue().set(String.format(jwtConfig.redisPrefix,userId), info.rePasswordDate?.time.toString())
                        info.rePasswordDate!!
                    } else {
                        Date(rePasswordDateStr?.toLong()!!)
                    }
                    if (DateUtil.getDistanceTimestamp(Date(), exp) < 0) {
                        throw ProgramException("用户登录已过期", 401)
                    }
                    if (StringUtils.isEmpty(userId)) {
                        throw ProgramException("请重新登录", 401)
                    }
                    if (DateUtil.getDistanceTimestamp(rePasswordDate, nbf) < 0) {
                        redisTemplate.opsForValue().set(String.format(jwtConfig.redisPrefix,userId), "")
                        throw ProgramException("请重新登录", 401)
                    }
                    request.setAttribute("userId", userId)
                } catch (e: Exception) {
                    throw  e as? ProgramException ?: ProgramException("请重新登录", 401)
                }
            }
        }
        return true
    }

    @Throws(Exception::class)
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, @Nullable modelAndView: ModelAndView?) {
    }

    @Throws(Exception::class)
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, @Nullable ex: java.lang.Exception?) {
    }
}
