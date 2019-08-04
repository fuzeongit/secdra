package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.dao.DrawDocumentDAO
import com.junjie.secdraservice.document.DrawDocument
import com.junjie.secdraservice.service.DrawDocumentService
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.query.QueryBuilders
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service
import java.util.*

@Service
class DrawDocumentServiceImpl(private val drawDocumentDAO: DrawDocumentDAO, private val elasticsearchTemplate: ElasticsearchTemplate) : DrawDocumentService {
    override fun paging(pageable: Pageable, tagList: List<String>?, precise: Boolean, name: String?, startDate: Date?, endDate: Date?, userId: String?, self: Boolean): Page<DrawDocument> {
        val mustQuery = QueryBuilders.boolQuery()
        if (tagList != null && !tagList.isEmpty()) {
            val tagBoolQuery = QueryBuilders.boolQuery()
            for (tag in tagList) {
                tagBoolQuery.must(QueryBuilders.matchPhraseQuery("tagList", tag).analyzer("ik_smart"))
            }
            mustQuery.must(tagBoolQuery);
        }
        if (!name.isNullOrEmpty())
            mustQuery.must(QueryBuilders.matchQuery("name", name));
        if (startDate != null || endDate != null) {
            val rangeQueryBuilder = QueryBuilders.rangeQuery("name")
            if (startDate != null)
                rangeQueryBuilder.from(startDate)
            if (endDate != null)
                rangeQueryBuilder.to(endDate)
            mustQuery.must(QueryBuilders.matchPhraseQuery("createDate", rangeQueryBuilder));
        }
        if (userId.isNullOrEmpty() || !self) {
            mustQuery.must(QueryBuilders.termQuery("privacy", PrivacyState.PUBLIC.toString()))
        }
        return drawDocumentDAO.search(mustQuery, pageable)
    }

    override fun countByTag(tag: String): Long {
        val queryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.matchPhraseQuery("tagList", tag).analyzer("ik_smart"))
        val searchQuery = NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build()
        return elasticsearchTemplate.count(searchQuery, DrawDocument::class.java)
    }

}