package com.junjie.secdradata.index.primary.dao

import com.junjie.secdradata.index.primary.document.DrawDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface DrawDocumentDAO : ElasticsearchRepository<DrawDocument, String> {

}