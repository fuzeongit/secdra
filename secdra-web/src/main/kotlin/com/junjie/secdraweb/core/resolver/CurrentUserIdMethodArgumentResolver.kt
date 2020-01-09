package com.junjie.secdraweb.core.resolver

import com.junjie.secdracore.annotations.CurrentUserId
import org.springframework.core.MethodParameter
import org.springframework.util.StringUtils
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * @author fjj
 * 获取请求的userId的解释器
 */
class CurrentUserIdMethodArgumentResolver : HandlerMethodArgumentResolver {
    /**
     * 有CurrentUserId注解的进入解释
     */
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType.isAssignableFrom(String::class.java) && parameter.hasParameterAnnotation(CurrentUserId::class.java)
    }

    /**
     * 在webRequest获取属性userId
     */
    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): String {
        if (StringUtils.isEmpty(webRequest.getAttribute("userId", RequestAttributes.SCOPE_REQUEST))) {
            return "";
        }
        return webRequest.getAttribute("userId", RequestAttributes.SCOPE_REQUEST) as String
    }
}