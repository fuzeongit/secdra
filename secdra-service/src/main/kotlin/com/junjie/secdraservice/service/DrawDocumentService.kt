package com.junjie.secdraservice.service

import com.junjie.secdraservice.document.DrawDocument
import org.elasticsearch.search.aggregations.Aggregation
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface DrawDocumentService {
    fun get(id: String): DrawDocument

    fun paging(pageable: Pageable, tagList: List<String>?, precise: Boolean, name: String?, startDate: Date?, endDate: Date?, userId: String?, self: Boolean): Page<DrawDocument>

    fun pagingByRecommend(userId: String?, pageable: Pageable, startDate: Date?, endDate: Date?): Page<DrawDocument>

    fun countByTag(tag: String): Long

    fun getFirstByTag(tag: String): DrawDocument

    fun listTagTop30(): List<String>

    fun save(draw: DrawDocument): DrawDocument

    fun saveViewAmount(draw: DrawDocument, viewAmount: Long): DrawDocument

    fun saveLikeAmount(draw: DrawDocument, likeAmount: Long): DrawDocument

    fun saveAll(drawList: List<DrawDocument>): MutableIterable<DrawDocument>
}