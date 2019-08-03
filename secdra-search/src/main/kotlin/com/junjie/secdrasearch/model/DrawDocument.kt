package com.junjie.secdrasearch.model

import com.junjie.secdracore.constant.PrivacyState
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.util.*

@Document(indexName = "index_draw_search")
class DrawDocument {
    @Id
    @Field(type = FieldType.Keyword)
    var id: String? = null

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    var name: String? = null

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    var introduction: String? = null

    @Field(type = FieldType.Keyword)
    var url: String? = null

    @Field(type = FieldType.Keyword, index = false)
    var userId: String? = null

    @Field(type = FieldType.Keyword)
    var privacy: PrivacyState = PrivacyState.PUBLIC

    @Field(type = FieldType.Keyword)
    var viewAmount: Long = 0

    @Field(type = FieldType.Keyword)
    var likeAmount: Long = 0

    @Field(type = FieldType.Keyword)
    var width: Long = 0;

    @Field(type = FieldType.Keyword)
    var height: Long = 0;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    var tagList: MutableList<String> = mutableListOf()

    @Field(type = FieldType.Keyword)
    var createDate: Date = Date()

    @Field(type = FieldType.Keyword)
    var modifiedDate: Date = Date()
}