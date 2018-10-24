package com.junjie.secdraservice.service

import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.contant.DrawState
import com.junjie.secdraservice.dao.IDrawDao
import com.junjie.secdraservice.model.Draw
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DrawService(val drawDao: IDrawDao) : IDrawService {
    override fun paging(pageable: Pageable): Page<Draw> {
        val query = Draw()
        query.drawState = DrawState.PASS
        val matcher = ExampleMatcher.matching()
                .withMatcher("drawState", ExampleMatcher.GenericPropertyMatchers.exact())
        val example = Example.of(query, matcher)
        return drawDao.findAll(example, pageable)
    }

    override fun pagingByUserId(pageable: Pageable, userId: String, idPrivate: Boolean): Page<Draw> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(id: String, userId: String?): Draw {
        val draw = drawDao.findById(id).orElseThrow { ProgramException("图片不存在", 404) }
        if (draw.isPrivate && draw.userId != userId) {
            throw ProgramException("您无权查看该图片", 404)
        }
        return draw
    }

    override fun save(userId: String, url: String, introduction: String, isPrivate: Boolean): Draw {
        val draw = Draw()
        draw.userId = userId
        draw.url = url
        draw.introduction = introduction
        draw.isPrivate = isPrivate

        return drawDao.save(draw)
    }
}