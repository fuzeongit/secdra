package com.junjie.secdrasearch.serviceimpl

import com.junjie.secdracore.constant.PrivacyState
import com.junjie.secdrasearch.dao.DrawDocumentDAO
import com.junjie.secdrasearch.model.DrawDocument
import com.junjie.secdrasearch.service.DrawDocumentService
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.query.QueryBuilders
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class DrawDocumentServiceImpl(private val drawDocumentDAO: DrawDocumentDAO) : DrawDocumentService {
    override fun paging(pageable: Pageable, tagList: List<String>?, precise: Boolean, name: String?, startDate: Date?, endDate: Date?): Page<DrawDocument> {

        val mustQuery = QueryBuilders.boolQuery()
        mustQuery.must(QueryBuilders.matchAllQuery());
        if (tagList != null && !tagList.isEmpty()) {
            val tagListQuery = QueryBuilders.matchQuery("tagList", tagList)
            if (precise) {
                tagListQuery.operator(Operator.AND)
            }
            mustQuery.must(tagListQuery);
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
        mustQuery.must(QueryBuilders.termQuery("privacy", PrivacyState.PUBLIC))
        return drawDocumentDAO.search(mustQuery, pageable)
    }
}