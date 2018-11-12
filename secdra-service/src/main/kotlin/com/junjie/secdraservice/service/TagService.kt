package com.junjie.secdraservice.service

import com.junjie.secdraservice.dao.ITagDao
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.ArrayList
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Service
class TagService(private val tagDao: ITagDao) : ITagService {
    override fun listTagOrderByLikeAmount(): List<Tag> {
        val specification = Specification<Tag> { root, criteriaQuery, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            val joinDraw: Join<Tag, Draw> = root.join("draw", JoinType.INNER)
            criteriaQuery.groupBy(root.get<String>("name"))
                    .orderBy(criteriaBuilder.desc(criteriaBuilder.count(root.get<String>("name")))
                            , criteriaBuilder.desc(criteriaBuilder.sum(joinDraw.get<Number>("likeAmount"))))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        val tagListSource = tagDao.findAll(specification)
        val tagList = ArrayList<Tag>()
        var index = 0;
        for (tagSource in tagListSource) {
            if (index > 99) break;
            val specificationItem = Specification<Tag> { root, criteriaQuery, criteriaBuilder ->
                val predicatesList = ArrayList<Predicate>()
                val joinDraw: Join<Tag, Draw> = root.join("draw", JoinType.INNER)
                predicatesList.add(criteriaBuilder.equal(root.get<String>("name"), tagSource.name))
                criteriaQuery.orderBy(criteriaBuilder.desc(joinDraw.get<Number>("likeAmount")))
                criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
            }
            try {
                tagList.add(tagDao.findAll(specificationItem)[0])
            } catch (e: Exception) {
                println(e.message)
            }
            index++
        }
        return tagList
    }
}