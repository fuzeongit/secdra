package com.junjie.secdraweb.base.config

import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.cache.RedisCacheConfiguration
import java.time.Duration
import java.util.HashMap
import java.util.HashSet
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext


@Configuration
@EnableCaching
class RedisConfigurer : CachingConfigurerSupport() {
    // 缓存管理器
    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        val cacheNames = HashSet<String>()
        val configMap = HashMap<String, RedisCacheConfiguration>()
        //
        cacheNames.add("draw::pagingRand")
        cacheNames.add("draw::paging")
        cacheNames.add("tag::listTagOrderByLikeAmount")
        cacheNames.add("draw::getFirstByTag")
        // 对每个缓存空间应用不同的配置
        configMap["draw::pagingRand"] = redisCacheConfiguration.entryTtl(Duration.ofHours(12))
        configMap["draw::paging"] = redisCacheConfiguration.entryTtl(Duration.ofHours(1))
        configMap["draw::getFirstByTag"] = redisCacheConfiguration.entryTtl(Duration.ofHours(12))
        configMap["tag::listTagOrderByLikeAmount"] = redisCacheConfiguration.entryTtl(Duration.ofHours(12))
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration)
                .initialCacheNames(cacheNames)  // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .withInitialCacheConfigurations(configMap)
                .build()
    }

//    @Bean
//    fun cacheManager(factory: RedisConnectionFactory): CacheManager {
//        val cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .disableCachingNullValues()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
//        // 设置一个初始化的缓存空间set集合
//        val cacheNames = HashSet<String>()
//        val configMap = HashMap<String, RedisCacheConfiguration>()
//        cacheNames.add("draw::pagingRand")
//
//        // 对每个缓存空间应用不同的配置
//        configMap["draw::pagingRand"] = cacheConfiguration.entryTtl(Duration.ofSeconds(120))
//        return RedisCacheManager.builder(factory)     // 使用自定义的缓存配置初始化一个cacheManager
//                .initialCacheNames(cacheNames)  // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
//                .withInitialCacheConfigurations(configMap)
//                .build()
//    }
}