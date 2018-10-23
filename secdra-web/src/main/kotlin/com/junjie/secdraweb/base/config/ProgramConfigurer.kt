package com.junjie.secdraweb.base.config

import com.junjie.secdraweb.base.component.JwtConfig
import com.junjie.secdraweb.base.interceptor.AuthInterceptor
import com.junjie.secdraweb.base.resolver.CurrentUserIdMethodArgumentResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class ProgramConfigurer : WebMvcConfigurer{
    override fun addInterceptors(registry: InterceptorRegistry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**")
        super.addInterceptors(registry)
    }

    override fun  addArgumentResolvers(argumentResolvers :MutableList<HandlerMethodArgumentResolver> ) {
        argumentResolvers.add(currentUserMethodArgumentResolver())
        super.addArgumentResolvers(argumentResolvers);
    }

    @Bean
    internal fun authInterceptor(): AuthInterceptor {
        return AuthInterceptor(jwtConfig())
    }

    @Bean
    fun currentUserMethodArgumentResolver(): CurrentUserIdMethodArgumentResolver {
        return CurrentUserIdMethodArgumentResolver()
    }

    @Bean
    internal fun jwtConfig(): JwtConfig {
        return JwtConfig()
    }
}