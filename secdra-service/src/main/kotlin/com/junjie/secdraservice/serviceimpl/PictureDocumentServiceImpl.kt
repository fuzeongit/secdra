package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdradata.index.primary.dao.PictureDocumentDAO
import com.junjie.secdradata.index.primary.document.PictureDocument
import com.junjie.secdraservice.service.CollectionService
import com.junjie.secdraservice.service.PictureDocumentService
import com.junjie.secdraservice.service.FootprintService
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms
import org.springframework.cache.annotation.CacheEvict
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
class PictureDocumentServiceImpl(private val pictureDocumentDAO: PictureDocumentDAO,
                                 private val elasticsearchTemplate: ElasticsearchTemplate,
                                 private val collectionService: CollectionService,
                                 private val footprintService: FootprintService) : PictureDocumentService {
    @Cacheable("pictureDocument::get", key = "#id")
    override fun get(id: String): PictureDocument {
        return pictureDocumentDAO.findById(id).orElseThrow { NotFoundException("图片不存在") }
    }

    @CacheEvict("pictureDocument::get", key = "#id")
    override fun remove(id: String): Boolean {
        try {
            pictureDocumentDAO.deleteById(id)
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    @Cacheable("pictureDocument::paging")
    override fun paging(pageable: Pageable, tagList: List<String>?, precise: Boolean, name: String?, startDate: Date?, endDate: Date?, userId: String?, self: Boolean): Page<PictureDocument> {
        val mustQuery = QueryBuilders.boolQuery()
        if (tagList != null && tagList.isNotEmpty()) {
            val tagBoolQuery = QueryBuilders.boolQuery()
            for (tag in tagList) {
                if (precise) {
                    tagBoolQuery.must(QueryBuilders.termQuery("tagList", tag))
                } else {
                    tagBoolQuery.should(QueryBuilders.wildcardQuery("tagList", "*$tag*"))
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
        return pictureDocumentDAO.search(mustQuery, pageable)
    }

    override fun pagingByRecommend(userId: String?, pageable: Pageable, startDate: Date?, endDate: Date?): Page<PictureDocument> {
        val tagList = mutableListOf<String>()
        if (!userId.isNullOrEmpty()) {
            val collectionList = collectionService.pagingByUserId(userId!!, PageRequest.of(0, 5)).content
            for (collection in collectionList) {
                try {
                    tagList.addAll(get(collection.pictureId).tagList)
                } catch (e: NotFoundException) {
                }
            }
        }
        return paging(pageable, tagList, false, null, startDate, endDate, null, false)
    }

    @Cacheable("pictureDocument::countByTag", key = "#tag")
    override fun countByTag(tag: String): Long {
        val queryBuilder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.termQuery("tagList", tag))
        val searchQuery = NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build()
        return elasticsearchTemplate.count(searchQuery, PictureDocument::class.java)
    }

    @Cacheable("pictureDocument::getFirstByTag", key = "#tag")
    override fun getFirstByTag(tag: String): PictureDocument {
        return paging(
                PageRequest.of(0, 1, Sort(Sort.Direction.DESC, "likeAmount")),
                listOf(tag), true, null,
                null, null,
                null, false).content.first()
    }

    @Cacheable("pictureDocument::listTagTop30")
    override fun listTagTop30(): List<String> {
        val aggregationBuilders = AggregationBuilders.terms("tagList").field("tagList").size(30).showTermDocCountError(true)
        val query = NativeSearchQueryBuilder()
                .withIndices("index_picture_search")
                .addAggregation(aggregationBuilders)
                .build()
        return elasticsearchTemplate.query(query) {
            it.aggregations.get<StringTerms>("tagList").buckets.map { bucket ->
                bucket.keyAsString
            }
        } ?: listOf()
    }

    @CachePut("pictureDocument::get", key = "#picture.id")
    override fun save(picture: PictureDocument): PictureDocument {
        val source = pictureDocumentDAO.findById(picture.id!!).orElse(picture)
        picture.viewAmount = source.viewAmount
        picture.likeAmount = source.likeAmount
        return pictureDocumentDAO.save(picture)
    }

    @CachePut("pictureDocument::get", key = "#picture.id")
    override fun saveViewAmount(picture: PictureDocument, viewAmount: Long): PictureDocument {
        picture.viewAmount = viewAmount
        return pictureDocumentDAO.save(picture)
    }

    @CachePut("pictureDocument::get", key = "#picture.id")
    override fun saveLikeAmount(picture: PictureDocument, likeAmount: Long): PictureDocument {
        picture.likeAmount = likeAmount
        return pictureDocumentDAO.save(picture)
    }

    override fun saveAll(pictureList: List<PictureDocument>): MutableIterable<PictureDocument> {
        return pictureDocumentDAO.saveAll(pictureList.map {
            it.viewAmount = footprintService.countByPictureId(it.id!!)
            it.likeAmount = collectionService.countByPictureId(it.id!!)
            it
        })
    }
}