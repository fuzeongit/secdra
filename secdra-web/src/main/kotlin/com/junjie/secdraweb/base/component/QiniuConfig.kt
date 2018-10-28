package com.junjie.secdraweb.base.component

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
//@PropertySource("qiniu.yml")
@ConfigurationProperties("qiniu")
class QiniuConfig {
    var accessKey: String = ""

    var secretKey: String = ""

    var bucket: String = ""
}
