package com.junjie.secdracore.util

import java.net.InetAddress
import java.net.UnknownHostException
import javax.servlet.http.HttpServletRequest

object IpUtil {
    fun getIpAddress(request: HttpServletRequest): String {
        return try {
            var ipAddress = request.getHeader("x-forwarded-for")
            if (ipAddress.isEmpty() || "unknown".equals(ipAddress, ignoreCase = true)) {
                ipAddress = request.getHeader("Proxy-Client-IP")
            }
            if (ipAddress.isEmpty() || "unknown".equals(ipAddress, ignoreCase = true)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP")
            }
            if (ipAddress.isEmpty() || "unknown".equals(ipAddress, ignoreCase = true)) {
                ipAddress = request.remoteAddr
                if (ipAddress == "127.0.0.1") {
                    // 根据网卡取本机配置的IP
                    val internet = InetAddress.getLocalHost()
                    ipAddress = internet.hostAddress
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","))
                }
            }
            ipAddress
        } catch (e: Exception) {
            ""
        }
    }
}