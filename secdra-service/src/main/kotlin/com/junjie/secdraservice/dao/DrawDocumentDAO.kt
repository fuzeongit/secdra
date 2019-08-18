package com.junjie.secdraservice.dao

import com.junjie.secdraservice.document.DrawDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DrawDocumentDAO : ElasticsearchRepository<DrawDocument, String> {

}