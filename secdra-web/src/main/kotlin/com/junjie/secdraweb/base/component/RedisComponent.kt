package com.junjie.secdraweb.base.component

import com.junjie.secdracore.constant.RedisConstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import java.util.HashMap
import java.util.concurrent.TimeUnit

@Component
class RedisComponent @Autowired
constructor(val redisTemplate: StringRedisTemplate) {
   fun getOpenid(request: HttpServletRequest, key: String): String? {
        val cookieMap = readCookieMap(request)
        val cookie = cookieMap.getOrDefault(key, null)

        return get(String.format(RedisConstant.TOKEN_PREFIX, cookie!!.value))
    }

    fun getUserId(request: HttpServletRequest, key: String): String? {
        val openid = getOpenid(request, key)
        return get(String.format(RedisConstant.OPENID_PREFIX, openid))
    }

    operator fun set(key: String, value: String, expire: Long, timeUnit: TimeUnit) {
        redisTemplate.opsForValue().set(key, value, expire, timeUnit)
    }

    operator fun get(key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }

    private fun readCookieMap(request: HttpServletRequest): Map<String, Cookie> {
        val cookieMap = HashMap<String, Cookie>()
        val cookies = request.cookies        //获取所有的cookie值
        if (cookies != null) {
            for (cookie in cookies) {
                cookieMap[cookie.name] = cookie
            }
        }
        return cookieMap
    }
}
