package com.junjie.secdraweb.controller

import com.junjie.secdraservice.service.TagService
import com.junjie.secdraweb.vo.TagVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @author fjj
 * 标签的控制器
 */
@RestController
@RequestMapping("tag")
class TagController(private val tagService: TagService) {
    @GetMapping("/listTagOrderByLikeAmount")
    fun listTagOrderByLikeAmount(): ArrayList<TagVO> {
        val list = tagService.listTagOrderByLikeAmount()
        val voList = ArrayList<TagVO>()
        for (item in list) {
            voList.add(TagVO(item.name!!, item.draw?.url!!))
        }
        return voList
    }
}