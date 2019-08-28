package com.junjie.secdraweb.core.configurer

import com.fasterxml.jackson.databind.ObjectMapper
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.core.component.BaseConfig
import com.junjie.secdraweb.core.interceptor.WebSocketUserInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


/**
 * @author fjj
 * webSocket配置
 */
@Configuration
class WebSocketConfigurer(private val redisTemplate: StringRedisTemplate, private val userService: UserService) : WebSocketMessageBrokerConfigurer {

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        //配置在webSocket二级目录下
        registry.addEndpoint("/webSocket").setAllowedOrigins("*").withSockJS()
    }

    /**
     * 配置信息代理
     */

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
//        // 订阅Broker名称
//        registry.enableSimpleBroker("/queue", "/topic");
//        // 全局使用的消息前缀（客户端订阅路径上会体现出来）
//        registry.setApplicationDestinationPrefixes("/app");
//        // 点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
//        registry.setUserDestinationPrefix("/user/");
    }

    /**
     * 配置客户端入站通道拦截器
     */
    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(webSocketUserInterceptor())
    }

    @Bean
    internal fun webSocketUserInterceptor(): WebSocketUserInterceptor {
        return WebSocketUserInterceptor(baseConfig(), redisTemplate, userService)
    }

    @Bean
    internal fun baseConfig(): BaseConfig {
        return BaseConfig()
    }

    override fun configureMessageConverters(messageConverters: MutableList<MessageConverter>): Boolean {
//        messageConverters.add(mappingJackson2MessageConverter())
        return true
    }


    @Bean
    fun mappingJackson2MessageConverter(): MappingJackson2MessageConverter {
        val converter = MappingJackson2MessageConverter();
        //设置日期格式
        val objectMapper = ObjectMapper();
//        val smt = SimpleDateFormat("yyyy-MM-dd");
//        objectMapper.dateFormat = smt;
        converter.objectMapper = objectMapper;
//        //设置中文编码格式
//       val list = ArrayList<MediaType>();
//        list.add(MediaType.APPLICATION_JSON_UTF8);
//        mappingJackson2MessageConverter.
        return converter;
    }
//    @Bean
//    fun converter(): StringMessageConverter {
//        val converter = StringMessageConverter();
//        return converter;
//    }
}


