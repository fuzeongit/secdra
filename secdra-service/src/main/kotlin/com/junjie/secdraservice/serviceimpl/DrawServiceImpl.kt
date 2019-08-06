package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdraservice.constant.DrawState
import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.dao.DrawDAO
import com.junjie.secdraservice.dao.DrawDocumentDAO
import com.junjie.secdraservice.document.DrawDocument
import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import com.junjie.secdraservice.service.CollectionService
import com.junjie.secdraservice.service.DrawDocumentService
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
class DrawServiceImpl(private val drawDAO: DrawDAO, private val drawDocumentDAO: DrawDocumentDAO, private val drawDocumentService: DrawDocumentService, private val collectionService: CollectionService) : DrawService {
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
            predicatesList.add(criteriaBuilder.equal(root.get<Int>("privacy"), PrivacyState.PUBLIC))
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
                predicatesList.add(criteriaBuilder.equal(root.get<String>("privacy"), PrivacyState.PUBLIC))
            }
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        return drawDAO.findAll(specification, pageable)
    }

    @Cacheable("draw::pagingRand")
    override fun pagingRand(pageable: Pageable): Page<Draw> {
        return drawDAO.pagingRand(pageable)
    }

    override fun countByTag(tag: String): Long {
        val specification = Specification<Draw> { root, criteriaQuery, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            val joinTag: Join<Draw, Tag> = root.join("tagList", JoinType.LEFT)
            predicatesList.add(criteriaBuilder.like(joinTag.get<String>("name"), "%$tag%"))
            predicatesList.add(criteriaBuilder.equal(root.get<String>("drawState"), DrawState.PASS))
            criteriaQuery.distinct(true)
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        return drawDAO.count(specification)
    }

    //    @Cacheable("draw::get", key = "#id")
    override fun get(id: String): Draw {
        return drawDAO.findById(id).orElseThrow { NotFoundException("图片不存在") }
    }

    //    @CachePut("draw::get", key = "#draw.id")
    override fun save(draw: Draw): DrawDocument {
        //TODO viewAmount
        return drawDocumentService
                .save(DrawDocument(drawDAO.save(draw), 0, collectionService.countByDrawId(draw.id!!)))
    }

    override fun synchronizationIndexDraw(): Long {
        val sourceList = drawDAO.findAll()
        val drawList = mutableListOf<DrawDocument>()
        for (source in sourceList) {
            //TODO viewAmount
            val draw = DrawDocument(source, 0, collectionService.countByDrawId(source.id!!))
            drawList.add(draw)
        }
        drawDocumentDAO.saveAll(drawList)
        return sourceList.size.toLong()
    }
}