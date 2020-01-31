package com.junjie.secdraadmin.controller

import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdraservice.service.SpecialCodeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("specialCode")
class SpecialCodeController(private val specialCodeService: SpecialCodeService) {
    @GetMapping("list")
    @RestfulPack
    fun list() = specialCodeService.list()

    @PostMapping("save")
    @RestfulPack
    fun save(code: String) = specialCodeService.save(code)

    @PostMapping("remove")
    @RestfulPack
    fun remove(id: String) = specialCodeService.remove(id)
}