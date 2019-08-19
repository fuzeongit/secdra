package com.junjie.secdraweb.base.interceptor

import com.junjie.secdracore.exception.SignInException
import com.junjie.secdracore.util.DateUtil
import com.junjie.secdracore.util.JwtUtil
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.base.component.BaseConfig
import com.junjie.secdraweb.model.WebSocketUser
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import java.util.*


class WebSocketUserInterceptor(private val baseConfig: BaseConfig, private val redisTemplate: StringRedisTemplate, val userService: UserService) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
        if (StompCommand.CONNECT == accessor!!.command) {
            try {
                //获取头信息
                val raw = message.headers[SimpMessageHeaderAccessor.NATIVE_HEADERS] as Map<*, *>
//                val token = raw["token"] as LinkedList<*>
                val token = (raw["token"] as LinkedList<*>)[0].toString()

                val claims = JwtUtil.parseJWT(token, baseConfig.jwtSecretString)
                val userId = claims["userId"] as String
                //过期时间
                val exp = Date(claims["exp"]?.toString()?.toLong()!! * 1000)
                //生成时间
                val nbf = Date(claims["nbf"]?.toString()?.toLong()!! * 1000)
                //最后更改密码时间
                val rePasswordDateStr = redisTemplate.opsForValue()[String.format(baseConfig.updatePasswordTimePrefix, userId)]
                val rePasswordDate: Date?
                //缓存穿透
                rePasswordDate = if (rePasswordDateStr.isNullOrEmpty()) {
                    val info = userService.getInfo(userId)
                    //最后更改密码时间写入redis
                    redisTemplate.opsForValue().set(
                            String.format(baseConfig.updatePasswordTimePrefix, userId),
                            info.rePasswordDate.time.toString())
                    info.rePasswordDate
                } else {
                    Date(rePasswordDateStr?.toLong()!!)
                }
                if (DateUtil.getDistanceTimestamp(Date(), exp) < 0) {
                    throw SignInException("用户登录已过期")
                }
                if (userId.isEmpty()) {
                    throw SignInException("请重新登录")
                }
                if (DateUtil.getDistanceTimestamp(rePasswordDate, nbf) < 0) {
                    redisTemplate.opsForValue().set(String.format(baseConfig.updatePasswordTimePrefix, userId), "")
                    throw SignInException("请重新登录")
                }
                accessor.user = WebSocketUser(userId)
            } catch (e: Exception) {
                accessor.user = WebSocketUser(baseConfig.notUUID)
            }
        } else if (StompCommand.DISCONNECT == accessor.command) {
            //点击断开连接，这里会执行两次，第二次执行的时候，message.getHeaders.size()=5,第一次是6。直接关闭浏览器，只会执行一次，size是5。
            //println("断开连接 --- 拦截器")
            //val vo = message.headers[SimpMessageHeaderAccessor.USER_HEADER] as WebSocketUser
        }
        return message
    }
}