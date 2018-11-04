package com.junjie.secdraweb.base.component

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("base")
class BaseConfig {
    var verificationCodePrefix: String = ""

    var jwtExpiresSecond: Long = 0

    var jwtBase64Secret: String = ""

    var updatePasswordTimePrefix: String = ""

    var qiniuAccessKey :String = ""

    var qiniuSecretKey :String = ""

    var qiniuBucket :String = ""
}
