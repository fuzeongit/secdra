package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.constant.DrawState
import com.junjie.secdraservice.dao.TagDAO
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.TagService
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Service
class TagServiceImpl(private val tagDAO: TagDAO) : TagService {
    @Cacheable("tag::listTagOrderByLikeAmount")
    override fun listTagOrderByLikeAmount(): List<Tag> {
        val specification = Specification<Tag> { root, criteriaQuery, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            val joinDraw: Join<Tag, Draw> = root.join("draw", JoinType.INNER)
            predicatesList.add(criteriaBuilder.equal(joinDraw.get<Int>("drawState"), DrawState.PASS))
            predicatesList.add(criteriaBuilder.equal(joinDraw.get<Int>("isPrivate"), false))
            criteriaQuery
                    .groupBy(root.get<String>("name"))
                    .orderBy(criteriaBuilder.desc(criteriaBuilder.count(root.get<String>("name")))
                            , criteriaBuilder.desc(criteriaBuilder.sum(joinDraw.get<Number>("likeAmount"))))
                    .distinct(true)
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }

        val tagListSource = tagDAO.findAll(specification, PageRequest.of(0, 30))
        return tagListSource.content
    }
}