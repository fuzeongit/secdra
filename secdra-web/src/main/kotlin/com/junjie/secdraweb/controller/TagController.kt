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

    @GetMapping("/test")
    fun test(): Boolean {
        val arr = arrayOf("可爱", "小可爱", "女孩子", "pixiv", "p站", "插画", "黑发", "诱惑", "原创", "好看", "雪", "泳装", "新年", "恭喜发财", "魅惑", "创作", "旗袍").toList()
        val list = drawDao.findAll()
        for (item in list) {
            val random = Random().nextInt(3) % 3 + 1;
            Collections.shuffle(arr);

            var i = 0
            while (i < random) {
                val tag = Tag()
                tag.name = arr[i]
                item.tagList!!.add(tag)
                drawDao.save(item)
                i++
            }
        }
        return true
    }
}