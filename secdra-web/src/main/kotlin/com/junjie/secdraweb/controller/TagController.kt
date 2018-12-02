package com.junjie.secdraweb.controller

import com.junjie.secdraservice.dao.IDrawDao
import com.junjie.secdraservice.dao.ITagDao
import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.ITagService
import com.junjie.secdraweb.vo.TagVo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @author fjj
 * 标签的控制器
 */
@RestController
@RequestMapping("/tag")
class TagController(private val tagService: ITagService, private val drawDao: IDrawDao, private val tagDao: ITagDao) {
    @GetMapping("/listTagOrderByLikeAmount")
    fun listTagOrderByLikeAmount(): ArrayList<TagVo> {
        val list = tagService.listTagOrderByLikeAmount()
        val voList = ArrayList<TagVo>()
        for (item in list) {
            voList.add(TagVo(item.name!!, item.draw?.url!!))
        }
        return voList
    }

    @GetMapping("/getFirst")
    fun get(): Tag {
        val id = "402880e566dd80a90166ddb27bc100e8"
        return tagDao.getOne(id)
    }
}