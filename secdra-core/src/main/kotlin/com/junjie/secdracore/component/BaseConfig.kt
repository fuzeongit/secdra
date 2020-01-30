package com.junjie.secdracore.component

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * @author fjj
 * 配置
 */
@Component
@ConfigurationProperties("base")
class BaseConfig {
    lateinit var notUUID: String
}
