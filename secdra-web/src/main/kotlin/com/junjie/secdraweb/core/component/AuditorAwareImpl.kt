package com.junjie.secdraweb.core.component

import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*


@Component
class AuditorAwareImpl : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        return Optional.ofNullable(try {
            (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?)!!.request.getAttribute("userId") as String
        } catch (e: Exception) {
            null
        })
    }
}