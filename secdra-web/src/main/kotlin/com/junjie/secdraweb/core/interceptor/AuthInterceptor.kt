package com.junjie.secdraweb.core.interceptor

import com.junjie.secdraaccount.core.component.AccountConfig
import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.SignInException
import com.junjie.secdracore.util.CookieUtil
import com.junjie.secdracore.util.DateUtil
import com.junjie.secdracore.util.JwtUtil
import com.junjie.secdraservice.service.AuthorizeCodeService
import com.junjie.secdraservice.service.UserService
import org.springframework.lang.Nullable
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author fjj
 * 登录验证拦截器
 */
class AuthInterceptor(private val accountConfig: AccountConfig,
                      private val accountService: AccountService,
                      private val userService: UserService,
                      private val authorizeCodeService: AuthorizeCodeService
) : HandlerInterceptor {
    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            val handlerMethod = handler as HandlerMethod?
            //程序运行的bean
            handlerMethod!!.beanType
            //运行的方法
            val method = handlerMethod.method
            val auth = method.getAnnotation(Auth::class.java)
            try {
                val cookieMap = CookieUtil.readCookieMap(request)
                val tokenCookie = cookieMap["token"]
                val token = if (tokenCookie != null) {
                    tokenCookie.value
                } else {
                    request.getHeader("token")
                }
                val claims = JwtUtil.parseJWT(token, accountConfig.jwtSecretString)
                val accountId = claims["id"] as String
                //过期时间
                val exp = Date(claims["exp"]!!.toString().toLong() * 1000)
                //生成时间
                val nbf = Date(claims["nbf"]!!.toString().toLong() * 1000)

                val account = accountService.get(accountId)

                if (DateUtil.getDistanceTimestamp(Date(), exp) < 0) {
                    throw SignInException("用户登录已过期")
                }
                if (accountId.isEmpty()) {
                    throw SignInException("请重新登录")
                }
                if (DateUtil.getDistanceTimestamp(account.lastModifiedDate!!, nbf) < 0) {
                    throw SignInException("请重新登录")
                }

                if (auth != null && auth.needCode) {
                    val authorizeCodeCookie = cookieMap["authorizeCode"]
                    val authorizeCode = (if (authorizeCodeCookie != null) {
                        authorizeCodeCookie.value
                    } else {
                        request.getHeader("authorizeCodeCookie")
                    }) ?: throw PermissionException("该操作需要特殊授权码进行")
                    try {
                        authorizeCodeService.getByCode(authorizeCode)
                    } catch (e: Exception) {
                        throw PermissionException("该操作的特殊授权码不合法")
                    }
                }
                request.setAttribute("userId", userService.getByAccountId(accountId).id!!)
            } catch (e: Exception) {
                if (method.isAnnotationPresent(Auth::class.java)) {
                    throw if (e is SignInException || e is PermissionException) {
                        e
                    } else SignInException("请重新登录")
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
