package com.junjie.secdraweb.core.configurer

import com.junjie.secdraaccount.core.component.AccountConfig
import com.junjie.secdraweb.core.interceptor.AuthInterceptor
import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdraservice.service.UserService
import com.junjie.secdraweb.core.component.BaseConfig
import com.junjie.secdraweb.core.component.RedisComponent
import com.junjie.secdraweb.core.resolver.CurrentUserIdMethodArgumentResolver
import com.junjie.secdraweb.service.QiniuComponent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author fjj
 * 程序的配置清单
 */
@Configuration
class ProgramConfigurer(private val redisTemplate: StringRedisTemplate,
                        private val accountService: AccountService,
                        private val userService: UserService) : WebMvcConfigurer {
    /**
     * 拦截器
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**")
        super.addInterceptors(registry)
    }

    /**
     * 解析器
     */
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(currentUserMethodArgumentResolver())
        super.addArgumentResolvers(argumentResolvers)
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
                .maxAge(3600)
    }

    @Bean
    internal fun authInterceptor(): AuthInterceptor {
        return AuthInterceptor(accountConfig(), redisTemplate, accountService, userService)
    }

    @Bean
    fun currentUserMethodArgumentResolver(): CurrentUserIdMethodArgumentResolver {
        return CurrentUserIdMethodArgumentResolver()
    }

    @Bean
    internal fun baseConfig(): BaseConfig {
        return BaseConfig()
    }

    @Bean
    internal fun accountConfig(): AccountConfig {
        return AccountConfig()
    }

    @Bean
    internal fun redisComponent(): RedisComponent {
        return RedisComponent(StringRedisTemplate())
    }

    @Bean
    internal fun qiniuComponent(): QiniuComponent {
        return QiniuComponent(baseConfig())
    }

//    @Bean
//    internal fun schedulingComponent(): SchedulingComponent {
//        return SchedulingComponent()
//    }

    @Bean
    fun dateConvert(): Converter<String, Date> {
        val formatStringList = arrayOf("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH", "yyyy-MM-dd", "yyyy-MM", "yyyy")
        return Converter { source ->
            var date: Date? = null
            for (formatString in formatStringList) {
                val sdf = SimpleDateFormat(formatString)
                try {
                    date = sdf.parse(source)
                    break
                } catch (e: Exception) {
                }
            }
            date
        }
    }
}