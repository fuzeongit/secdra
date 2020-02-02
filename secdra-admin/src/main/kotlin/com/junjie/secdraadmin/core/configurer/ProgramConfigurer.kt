package com.junjie.secdraadmin.core.configurer

import com.junjie.secdraadmin.core.component.RedisComponent
import com.junjie.secdraadmin.core.interceptor.AuthInterceptor
import com.junjie.secdraqiniu.core.component.QiniuConfig
import com.junjie.secdraservice.service.AdministratorService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.redis.core.StringRedisTemplate
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
class ProgramConfigurer(private val administratorService: AdministratorService) : WebMvcConfigurer {
    /**
     * 拦截器
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(authInterceptor())
                .addPathPatterns("/collect/*")
                .addPathPatterns("/picture/*")
                .addPathPatterns("/specialCode/*")
                .addPathPatterns("/user/*")
        super.addInterceptors(registry)
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
                //设置允许的方法
                .allowedHeaders("*")
                //跨域允许时间
                .maxAge(3600)
    }

    /**
     * 七牛配置
     */
    @Bean
    internal fun qiniuConfig(): QiniuConfig {
        return QiniuConfig()
    }

    @Bean
    internal fun redisComponent(): RedisComponent {
        return RedisComponent(StringRedisTemplate())
    }

    @Bean
    internal fun authInterceptor(): AuthInterceptor {
        return AuthInterceptor(administratorService)
    }

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