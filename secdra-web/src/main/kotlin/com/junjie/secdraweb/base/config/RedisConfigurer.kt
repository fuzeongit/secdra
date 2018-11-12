package com.junjie.secdraweb.base.config

import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration


@Configuration
@EnableCaching
class RedisConfigurer : CachingConfigurerSupport() {

}