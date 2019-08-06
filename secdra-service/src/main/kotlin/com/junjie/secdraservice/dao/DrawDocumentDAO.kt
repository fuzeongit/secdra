package com.junjie.secdraservice.dao

import com.junjie.secdraservice.document.DrawDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import java.util.*

interface DrawDocumentDAO : ElasticsearchRepository<DrawDocument, String> {

}