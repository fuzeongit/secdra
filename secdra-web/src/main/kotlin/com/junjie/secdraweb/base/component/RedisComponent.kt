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
class RedisComponent(val redisTemplate: StringRedisTemplate)
{
    fun set(key: String, value: String, expire: Long, timeUnit: TimeUnit) {
        redisTemplate.opsForValue().set(key, value, expire, timeUnit)
    }

    fun get(key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }
}