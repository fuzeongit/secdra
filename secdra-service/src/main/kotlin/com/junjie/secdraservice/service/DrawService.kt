package com.junjie.secdraservice.service

import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.contant.DrawState
import com.junjie.secdraservice.dao.IDrawDao
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import com.qiniu.util.StringUtils
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate


@Service
class DrawService(val drawDao: IDrawDao) : IDrawService {
    override fun paging(pageable: Pageable): Page<Draw> {
        val query = Draw()
        query.drawState = DrawState.PASS
        val matcher = ExampleMatcher.matching()
                .withMatcher("drawState", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("viewAmount")
                .withIgnorePaths("likeAmount")
                .withIgnorePaths("width")
                .withIgnorePaths("height")
        val example = Example.of(query, matcher)
        return drawDao.findAll(example, pageable)
    }

    override fun pagingByTag(pageable: Pageable, tag: String): Page<Draw> {
        val specification = Specification<Draw> { root, criteriaQuery, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            val joinTag: Join<Draw, Tag> = root.join("tagList", JoinType.LEFT)
            predicatesList.add(criteriaBuilder.like(joinTag.get<String>("name"), "%$tag%"))
            predicatesList.add(criteriaBuilder.equal(root.get<String>("drawState"), DrawState.PASS))
            predicatesList.add(criteriaBuilder.equal(root.get<String>("isPrivate"), false))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        return drawDao.findAll(specification, pageable)
    }


    override fun pagingByUserId(pageable: Pageable, userId: String, isSelf: Boolean): Page<Draw> {
        val query = Draw()
        query.drawState = DrawState.PASS
        var matcher = ExampleMatcher.matching()
                .withMatcher("drawState", ExampleMatcher.GenericPropertyMatchers.exact())
        //如果是自己排除
        if (isSelf) {
            matcher = matcher.withIgnorePaths("isPrivate");
        }
        val example = Example.of(query, matcher)
        return drawDao.findAll(example, pageable)
    }

    override fun get(id: String, userId: String?): Draw {
        val draw = drawDao.findById(id).orElseThrow { ProgramException("图片不存在", 404) }
        if (draw.drawState != DrawState.PASS) {
            ProgramException("该图片已被屏蔽", 403)
        }
        if (draw.isPrivate && draw.userId != userId) {
            throw ProgramException("您无权查看该图片", 403)
        }
        return draw
    }

    override fun update(userId: String, drawId: String, introduction: String?, isPrivate: Boolean): Draw {
        val draw = drawDao.findById(drawId).orElseThrow { ProgramException("图片不存在") }
        if (draw.drawState != DrawState.PASS) {
            ProgramException("该图片已被屏蔽", 403)
        }
        if (draw.userId != userId) {
            throw ProgramException("您无权修改该图片", 403)
        }
        if (StringUtils.isNullOrEmpty(introduction)) {
            draw.introduction = introduction
        }
        draw.isPrivate = isPrivate
        return drawDao.save(draw)
    }

    override fun save(userId: String, url: String, introduction: String?, isPrivate: Boolean): Draw {
        val draw = Draw()

        draw.userId = userId;
        draw.url = url;
        draw.introduction = introduction;
        draw.isPrivate = isPrivate;

        return drawDao.save(draw)
    }

}