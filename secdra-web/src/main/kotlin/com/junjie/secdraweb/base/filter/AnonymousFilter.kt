package com.junjie.secdraweb.base.filter

import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
@WebFilter(filterName = "ResponseFilter", urlPatterns = ["/*"])
class AnonymousFilter : Filter {
    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        println("过滤器运行")
        val httpRequest = servletRequest as HttpServletRequest
        val httpResponse = servletResponse as HttpServletResponse

        filterChain.doFilter(httpRequest, httpResponse)
    }

    override fun destroy() {}
}