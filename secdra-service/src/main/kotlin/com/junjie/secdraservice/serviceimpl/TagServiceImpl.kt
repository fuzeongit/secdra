package com.junjie.secdraservice.serviceimpl

import com.junjie.secdradata.constant.PictureState
import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdradata.database.primary.dao.TagDAO
import com.junjie.secdradata.database.primary.entity.Picture
import com.junjie.secdradata.database.primary.entity.Tag
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
            val joinPicture: Join<Tag, Picture> = root.join("picture", JoinType.INNER)
            predicatesList.add(criteriaBuilder.equal(joinPicture.get<Int>("pictureState"), PictureState.PASS))
            predicatesList.add(criteriaBuilder.equal(joinPicture.get<Int>("privacy"), PrivacyState.PUBLIC))
            criteriaQuery
                    .groupBy(root.get<String>("name"))
                    .orderBy(criteriaBuilder.desc(criteriaBuilder.count(root.get<String>("name")))
                            , criteriaBuilder.desc(criteriaBuilder.sum(joinPicture.get<Number>("likeAmount"))))
                    .distinct(true)
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }

        val tagListSource = tagDAO.findAll(specification, PageRequest.of(0, 30))
        return tagListSource.content
    }
}