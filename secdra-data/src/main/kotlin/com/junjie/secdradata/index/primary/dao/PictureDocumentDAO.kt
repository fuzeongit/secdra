package com.junjie.secdradata.index.primary.dao

import com.junjie.secdradata.index.primary.document.PictureDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface PictureDocumentDAO : ElasticsearchRepository<PictureDocument, String> {

}