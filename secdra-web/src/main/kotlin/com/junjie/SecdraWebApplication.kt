package com.junjie

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@SpringBootApplication
@EnableJpaAuditing
class SecdraWebApplication : SpringBootServletInitializer() {
    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
        // 设置启动类,用于独立tomcat运行的入口
        return builder.sources(SecdraWebApplication::class.java)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<SecdraWebApplication>(*args)
        }
    }
}


//@SpringBootApplication
//@EnableJpaAuditing
//class SecdraWebApplication fun main(args: Array<String>) {
//    runApplication<SecdraWebApplication>(*args)
//}

