package com.junjie.secdraweb.controller

import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdraservice.service.DrawDocumentService
import com.junjie.secdraweb.vo.TagDrawVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author fjj
 * 标签的控制器
 */
@RestController
@RequestMapping("tag")
class TagController(private val drawDocumentService: DrawDocumentService) {
    @GetMapping("/listTagTop30")
    @RestfulPack
    fun listTagTop30(): List<String> {
        return drawDocumentService.listTagTop30()
    }

    @GetMapping("/listTagAndDrawTop30")
    @RestfulPack
    fun listTagAndDrawTop30(): List<TagDrawVO> {
        val tagList = drawDocumentService.listTagTop30()
        return tagList.map {
            val draw = drawDocumentService.getFirstByTag(it)
            TagDrawVO(draw.url, it)
        }
    }
}