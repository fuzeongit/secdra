package com.junjie.secdrasearch.model

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.util.*

@Document(indexName = "index_draw")
class IndexDraw {
    @Id
    var id: String? = null

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    var name: String? = null

    @Field(type = FieldType.Keyword, index = false)
    var userId: String? = null

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    var tagList: MutableList<String> = mutableListOf()

    @Field(type = FieldType.Keyword)
    var viewAmount: Long = 0

    @Field(type = FieldType.Keyword)
    var likeAmount: Long = 0

    @Field(type = FieldType.Keyword)
    var createDate: Date = Date()
}