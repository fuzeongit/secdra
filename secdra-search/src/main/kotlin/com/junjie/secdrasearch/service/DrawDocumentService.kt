package com.junjie.secdrasearch.service

import com.junjie.secdrasearch.model.DrawDocument
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface DrawDocumentService {
    fun paging(pageable: Pageable, tagList: List<String>?, precise: Boolean, name: String?, startDate: Date?, endDate: Date?): Page<DrawDocument>
}