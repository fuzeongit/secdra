package com.junjie.secdracore.util

import java.util.HashMap
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

object CookieUtil {
    /**
     * 将cookie封装成map
     *
     * @param request 请求体
     * @return cookie的map
     */
    fun readCookieMap(request: HttpServletRequest): Map<String, Cookie> {
        val cookieMap = HashMap<String, Cookie>()
        val cookies = request.cookies        //获取所有的cookie值
        cookies?.let {
            for (cookie in it) {
                cookieMap[cookie.name] = cookie
            }
        }
        return cookieMap
    }
}