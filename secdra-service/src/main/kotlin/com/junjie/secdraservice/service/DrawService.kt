package com.junjie.secdraservice.service

import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.util.DateUtil
import com.junjie.secdraservice.contant.DrawState
import com.junjie.secdraservice.dao.IDrawDao
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import com.qiniu.util.StringUtils
import org.springframework.data.domain.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate


@Service
class DrawService(val drawDao: IDrawDao) : IDrawService {
    override fun paging(pageable: Pageable, name: String?, startDate: Date?, endDate: Date?): Page<Draw> {
        val specification = Specification<Draw> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            if (!StringUtils.isNullOrEmpty(name)) {
                val joinTag: Join<Draw, Tag> = root.join("tagList", JoinType.LEFT)
                predicatesList.add(criteriaBuilder.like(joinTag.get<String>("name"), "%$name%"))
            }
            if (startDate != null) {
                predicatesList.add(criteriaBuilder.greaterThan(root.get("createDate"), startDate))
            }
            if (endDate != null) {
                predicatesList.add(criteriaBuilder.lessThan(root.get("createDate"), endDate))
            }
            predicatesList.add(criteriaBuilder.equal(root.get<String>("drawState"), DrawState.PASS))
            predicatesList.add(criteriaBuilder.equal(root.get<String>("isPrivate"), false))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        return drawDao.findAll(specification, pageable)
    }

    override fun pagingByUserId(pageable: Pageable, userId: String, startDate: Date?, endDate: Date?, isSelf: Boolean): Page<Draw> {
        val specification = Specification<Draw> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            if (startDate != null) {
                predicatesList.add(criteriaBuilder.greaterThan(root.get("createDate"), startDate))
            }
            if (endDate != null) {
                predicatesList.add(criteriaBuilder.lessThan(root.get("createDate"), endDate))
            }
            predicatesList.add(criteriaBuilder.equal(root.get<String>("userId"), userId))
            predicatesList.add(criteriaBuilder.equal(root.get<String>("drawState"), DrawState.PASS))
            if (!isSelf) {
                predicatesList.add(criteriaBuilder.equal(root.get<String>("isPrivate"), false))
            }
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        return drawDao.findAll(specification, pageable)
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
        val draw = drawDao.findById(drawId).orElseThrow { ProgramException("图片不存在", 404) }
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

    override fun save(draw: Draw): Draw {
        return drawDao.save(draw)
    }

    override fun pagingRand(pageable:Pageable): Page<Draw> {
        return drawDao.pagingRand(pageable)
    }
}