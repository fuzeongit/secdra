package com.junjie.secdraservice.service

import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.contant.DrawState
import com.junjie.secdraservice.dao.ITagDao
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Service
class TagService(private val tagDao: ITagDao) : ITagService {
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
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }

        val tagListSource = tagDao.findAll(specification, PageRequest.of(0, 30))
        return tagListSource.content
//        val tagList = ArrayList<Tag>()
//        for (tagSource in tagListSource.content) {
//            val specificationItem = Specification<Tag> { root, criteriaQuery, criteriaBuilder ->
//                val predicatesList = ArrayList<Predicate>()
//                val joinDraw: Join<Tag, Draw> = root.join("draw", JoinType.INNER)
//                predicatesList.add(criteriaBuilder.equal(root.get<String>("name"), tagSource.name))
//                criteriaQuery.orderBy(criteriaBuilder.desc(joinDraw.get<Number>("likeAmount")))
//                criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
//            }
//            try {
//                val tagFirst = tagDao.findAll(specificationItem, PageRequest.of(0, 1)).content
//                tagList.add(tagFirst[0])
//            } catch (e: Exception) {
//                println(e.message)
//            }
//        }
//        return tagList
    }
}