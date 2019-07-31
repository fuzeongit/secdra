package com.junjie.secdrasearch.dao

import com.junjie.secdrasearch.model.IndexDraw
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface IndexDrawDAO : ElasticsearchRepository<IndexDraw, String> {
    fun findAllByNameOrTagList(pageable: Pageable, name: String, tagList: String): Page<IndexDraw>
}