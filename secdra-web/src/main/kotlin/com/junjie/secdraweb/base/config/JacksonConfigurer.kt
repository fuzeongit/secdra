package com.junjie.secdraweb.base.config

import com.junjie.secdracore.fragment.MixIn
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class JacksonConfigurer {
    @Bean
    fun addCustomBigDecimalDeserialization(): Jackson2ObjectMapperBuilderCustomizer {
        return Jackson2ObjectMapperBuilderCustomizer { jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.mixIn(StringTerms.Bucket::class.java, MixIn::class.java) }
    }

}
