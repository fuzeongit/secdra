package com.junjie.secdraweb.controller

import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.ITagService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tag")
class TagController(val tagService: ITagService) {
    @GetMapping("/listTagOrderByLikeAmount")
    fun listTagOrderByLikeAmount(): List<Tag> {
        val list = tagService.listTagOrderByLikeAmount()
        for (item in list) {
            println(item.draw?.id)
        }
        return list
    }
}