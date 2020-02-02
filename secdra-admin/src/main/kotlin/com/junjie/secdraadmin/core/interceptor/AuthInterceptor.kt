package com.junjie.secdraadmin.core.interceptor

import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdraservice.service.AdministratorService
import org.springframework.lang.Nullable
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author fjj
 * 后台拦截器
 */
class AuthInterceptor(private val administratorService: AdministratorService
) : HandlerInterceptor {
    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        return try {
            val accessKey = request.getHeader("accessKey")
            val secretKey = request.getHeader("secretKey")
            /**
             * 不用缓存，并发不高
             */
            administratorService.get(accessKey, secretKey)
            true
        } catch (e: Exception) {
            throw PermissionException("没有授权")
        }
    }

    @Throws(Exception::class)
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, @Nullable modelAndView: ModelAndView?) {
    }

    @Throws(Exception::class)
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, @Nullable ex: java.lang.Exception?) {
    }
}
