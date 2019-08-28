package com.junjie.secdraweb.core.configurer

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

/**
 * @author fjj
 * 自动化api文档配置
 */
@Configuration
class Swagger2Configurer {

    @Bean
    fun createRestApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.junjie.secdraweb.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("api文档")
                .description("")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}