package com.junjie

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class SecdraWebApplication fun main(args: Array<String>) {
    runApplication<SecdraWebApplication>(*args)
}