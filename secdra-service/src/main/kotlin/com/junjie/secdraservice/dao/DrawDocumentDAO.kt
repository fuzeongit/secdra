package com.junjie.secdraservice.dao

import com.junjie.secdraservice.document.DrawDocument
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface DrawDocumentDAO : ElasticsearchRepository<DrawDocument, String> {
    fun findAllByNameOrTagList(pageable: Pageable, name: String, tagList: String): Page<DrawDocument>
}