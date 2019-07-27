package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdraservice.constant.DrawState
import com.junjie.secdraservice.dao.DrawDAO
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.DrawService

import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate


@Service
class DrawServiceImpl(val drawDAO: DrawDAO) : DrawService {
    @Cacheable("draw::paging")
    override fun paging(pageable: Pageable, tag: String?, startDate: Date?, endDate: Date?): Page<Draw> {
        val specification = Specification<Draw> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            if (!tag.isNullOrEmpty()) {
                val joinTag: Join<Draw, Tag> = root.join("tagList", JoinType.LEFT)
                predicatesList.add(criteriaBuilder.like(joinTag.get<String>("name"), "%$tag%"))
            }
            if (startDate != null) {
                predicatesList.add(criteriaBuilder.greaterThan(root.get("createDate"), startDate))
            }
            if (endDate != null) {
                predicatesList.add(criteriaBuilder.lessThan(root.get("createDate"), endDate))
            }
            predicatesList.add(criteriaBuilder.equal(root.get<Int>("drawState"), DrawState.PASS))
            predicatesList.add(criteriaBuilder.equal(root.get<Int>("isPrivate"), false))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        return drawDAO.findAll(specification, pageable)
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
        return drawDAO.findAll(specification, pageable)
    }

    @Cacheable("draw::get", key = "#id")
    override fun get(id: String): Draw {
        return drawDAO.findById(id).orElseThrow { NotFoundException("图片不存在") }
    }

    @CachePut("draw::get", key = "#id")
    override fun update(id: String, viewAmount: Long?, likeAmount: Long?): Draw {
        val draw = get(id)
        if (viewAmount != null) {
            draw.viewAmount = viewAmount
        }
        if (likeAmount != null) {
            draw.likeAmount = likeAmount
        }
        return drawDAO.save(draw)
    }

    @CachePut("draw::get", key = "#draw.id")
    override fun save(draw: Draw): Draw {
        return drawDAO.save(draw)
    }

    @Cacheable("draw::pagingRand")
    override fun pagingRand(pageable: Pageable): Page<Draw> {
        return drawDAO.pagingRand(pageable)
    }

    @Cacheable("draw::getFirstByTag")
    override fun getFirstByTag(tag: String): Draw {
        return paging(PageRequest.of(0, 1, Sort(Sort.Direction.DESC, "likeAmount")), tag, null, null)
                .content.first()
    }

    override fun countByTag(tag: String): Long {
        val specification = Specification<Draw> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            val joinTag: Join<Draw, Tag> = root.join("tagList", JoinType.LEFT)
            predicatesList.add(criteriaBuilder.like(joinTag.get<String>("name"), "%$tag%"))
            predicatesList.add(criteriaBuilder.equal(root.get<String>("drawState"), DrawState.PASS))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        return drawDAO.count(specification)
    }
}