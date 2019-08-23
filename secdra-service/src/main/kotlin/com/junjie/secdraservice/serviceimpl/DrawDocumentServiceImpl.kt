package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.dao.DrawDocumentDAO
import com.junjie.secdraservice.document.DrawDocument
import com.junjie.secdraservice.service.CollectionService
import com.junjie.secdraservice.service.DrawDocumentService
import com.junjie.secdraservice.service.FootprintService
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.aggregations.Aggregation
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service
import java.util.*

@Service
class DrawDocumentServiceImpl(private val drawDocumentDAO: DrawDocumentDAO,
                              private val elasticsearchTemplate: ElasticsearchTemplate,
                              private val collectionService: CollectionService,
                              private val footprintService: FootprintService) : DrawDocumentService {

    @Cacheable("drawDocument::get", key = "#id")
    override fun get(id: String): DrawDocument {
        return drawDocumentDAO.findById(id).orElseThrow { NotFoundException("图片不存在") }
    }

    @Cacheable("drawDocument::paging")
    override fun paging(pageable: Pageable, tagList: List<String>?, precise: Boolean, name: String?, startDate: Date?, endDate: Date?, userId: String?, self: Boolean): Page<DrawDocument> {
        val mustQuery = QueryBuilders.boolQuery()
        if (tagList != null && tagList.isNotEmpty()) {
            val tagBoolQuery = QueryBuilders.boolQuery()
            for (tag in tagList) {
                if (precise) {
                    tagBoolQuery.must(QueryBuilders.termQuery("tagList", tag))
                } else {
                    tagBoolQuery.must(QueryBuilders.wildcardQuery("tagList", "*$tag*"))
                }
            }
            mustQuery.must(tagBoolQuery)
        }
        if (!name.isNullOrEmpty())
            mustQuery.must(QueryBuilders.matchPhraseQuery("name", name))
        if (startDate != null || endDate != null) {
            val rangeQueryBuilder = QueryBuilders.rangeQuery("createDate")
            startDate?.let { rangeQueryBuilder.from(it) }
            endDate?.let { rangeQueryBuilder.to(it) }
            mustQuery.must(rangeQueryBuilder)
        }
        if (userId.isNullOrEmpty() || !self) {
            mustQuery.must(QueryBuilders.termQuery("privacy", PrivacyState.PUBLIC.toString()))
        }
        if (!userId.isNullOrEmpty()) {
            mustQuery.must(QueryBuilders.termQuery("userId", userId))
        }
        return drawDocumentDAO.search(mustQuery, pageable)
    }

    @Cacheable("drawDocument::countByTag", key = "#tag")
    override fun countByTag(tag: String): Long {
        val queryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("tagList", tag))
        val searchQuery = NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build()
        return elasticsearchTemplate.count(searchQuery, DrawDocument::class.java)
    }

    @Cacheable("drawDocument::getFirstByTag", key = "#tag")
    override fun getFirstByTag(tag: String): DrawDocument {
        return paging(
                PageRequest.of(0, 1, Sort(Sort.Direction.DESC, "likeAmount")),
                listOf(tag), true, null,
                null, null,
                null, false).content.first()
    }

    @Cacheable("drawDocument::listTagTop30")
    override fun listTagTop30(): Aggregation? {
        val aggregationBuilders = AggregationBuilders.terms("tagList").field("tagList").size(30).showTermDocCountError(true)
        val query = NativeSearchQueryBuilder()
                .withIndices("index_draw_search")
                .addAggregation(aggregationBuilders)
                .build()
        return elasticsearchTemplate.query(query) {
            it.aggregations.get("tagList")
        }
    }

    @CachePut("drawDocument::save", key = "#draw.id")
    override fun save(draw: DrawDocument): DrawDocument {
        val source = drawDocumentDAO.findById(draw.id!!).orElse(draw)
        draw.viewAmount = source.viewAmount
        draw.likeAmount = source.likeAmount
        return drawDocumentDAO.save(draw)
    }

    @CachePut("drawDocument::save", key = "#draw.id")
    override fun saveViewAmount(draw: DrawDocument, viewAmount: Long): DrawDocument {
        draw.viewAmount = viewAmount
        return drawDocumentDAO.save(draw)
    }

    @CachePut("drawDocument::save", key = "#draw.id")
    override fun saveLikeAmount(draw: DrawDocument, likeAmount: Long): DrawDocument {
        draw.viewAmount = likeAmount
        return drawDocumentDAO.save(draw)
    }

    override fun saveAll(drawList: List<DrawDocument>): MutableIterable<DrawDocument> {
        return drawDocumentDAO.saveAll(drawList.map {
            it.viewAmount = footprintService.countByDrawId(it.id!!)
            it.likeAmount = collectionService.countByDrawId(it.id!!)
            it
        })
    }
}