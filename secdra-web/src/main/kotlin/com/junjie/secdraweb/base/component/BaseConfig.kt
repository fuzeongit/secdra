package com.junjie.secdraweb.base.component

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author fjj
 * 配置
 */
@Component
@ConfigurationProperties("base")
class BaseConfig {
    var verificationCodePrefix: String = ""

    var jwtExpiresSecond: Long = 0

    var jwtSecretString: String = ""
        get() {
            return Base64.getEncoder().encodeToString(field.toByteArray(charset("utf-8")))
        }

    var updatePasswordTimePrefix: String = ""

    var notUUID: String = ""

    var qiniuAccessKey: String = ""

    var qiniuSecretKey: String = ""
    //临时储存空间
    var qiniuTempBucket: String = ""
    //头像储存空间
    var qiniuHeadBucket: String = ""
    //背景储存空间
    var qiniuBackBucket: String = ""
    //主储存空间
    var qiniuBucket: String = ""
    //临时储存空间
    var qiniuTempBucketUrl: String = ""
    //头像储存空间
    var qiniuHeadBucketUrl: String = ""
    //背景储存空间
    var qiniuBackBucketUrl: String = ""
    //主储存空间
    var qiniuBucketUrl: String = ""
}
