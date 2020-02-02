package com.junjie.secdraadmin.controller

import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdraservice.service.AuthorizeCodeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("authorizeCode")
class AuthorizeCodeController(private val authorizeCodeService: AuthorizeCodeService) {
    @GetMapping("list")
    @RestfulPack
    fun list() = authorizeCodeService.list()

    @PostMapping("save")
    @RestfulPack
    fun save(code: String) = authorizeCodeService.save(code)

    @PostMapping("remove")
    @RestfulPack
    fun remove(id: String) = authorizeCodeService.remove(id)
}