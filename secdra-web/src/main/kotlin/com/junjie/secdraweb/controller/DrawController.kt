package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.CurrentUserId
import com.junjie.secdraservice.service.IDrawService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("draw")
class DrawController(val drawService: IDrawService) {
    @GetMapping("/get")
    fun get(id: String?, @CurrentUserId userId: String?) {
        drawService.get(id!!, userId)
    }

    @PostMapping("/save")
    @Auth
    fun save(@CurrentUserId userId: String, url: String, desc: String, isPrivate: Boolean) {
        drawService.save(userId, url, desc, isPrivate)
    }
}