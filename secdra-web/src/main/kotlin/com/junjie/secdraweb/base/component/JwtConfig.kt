package com.junjie.secdraweb.base.component

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@PropertySource("jwt.yml")
@ConfigurationProperties("jwt")
class JwtConfig {
    var expiresSecond:Long = 0

    var base64Secret:String = ""
}
