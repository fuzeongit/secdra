package com.junjie.secdraweb.base.config

import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner
import com.junjie.secdraservice.service.IUserService
import com.junjie.secdraweb.base.component.JwtConfig
import com.junjie.secdraweb.base.component.QiniuConfig
import com.junjie.secdraweb.base.component.RedisComponent
import com.junjie.secdraweb.base.interceptor.AuthInterceptor
import com.junjie.secdraweb.base.resolver.CurrentUserIdMethodArgumentResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class ProgramConfigurer(private val redisTemplate: StringRedisTemplate, private val userService: IUserService) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**")
        super.addInterceptors(registry)
    }

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(currentUserMethodArgumentResolver())
        super.addArgumentResolvers(argumentResolvers);
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        //设置允许跨域的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                .allowedOrigins("*")
                //是否允许证书 不再默认开启
                .allowCredentials(true)
                //设置允许的方法
                .allowedMethods("*")
                //跨域允许时间
                .maxAge(3600);
    }


    @Bean
    fun socketIOServer(): SocketIOServer {
        val config = com.corundumstudio.socketio.Configuration()
        config.hostname = "localhost"
        config.port = 8089
        return SocketIOServer(config);
    }

    @Bean
    fun springAnnotationScanner(socketServer: SocketIOServer): SpringAnnotationScanner {
        return SpringAnnotationScanner(socketServer);
    }

    @Bean
    internal fun authInterceptor(): AuthInterceptor {
        return AuthInterceptor(jwtConfig(), redisTemplate, userService)
    }

    @Bean
    fun currentUserMethodArgumentResolver(): CurrentUserIdMethodArgumentResolver {
        return CurrentUserIdMethodArgumentResolver()
    }

    @Bean
    internal fun jwtConfig(): JwtConfig {
        return JwtConfig()
    }

    @Bean
    internal fun qiniuConfig(): QiniuConfig {
        return QiniuConfig()
    }

    @Bean
    internal fun redisComponent(): RedisComponent {
        return RedisComponent(StringRedisTemplate())
    }
}