package com.junjie.secdraservice.document

import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.model.Draw
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.io.Serializable
import java.util.*

@Document(indexName = "index_draw_search")
class DrawDocument : Serializable {
    @Id
    @Field(type = FieldType.Keyword)
    var id: String? = null

    @Field(type = FieldType.Keyword)
    lateinit var userId: String

    @Field(type = FieldType.Keyword, index = false)
    lateinit var url: String

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    var name: String = "无题"

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    var introduction: String = "身无彩凤双飞翼，心有灵犀一点通"

    @Field(type = FieldType.Keyword)
    var privacy: PrivacyState = PrivacyState.PUBLIC

    @Field(type = FieldType.Keyword)
    var viewAmount: Long = 0

    @Field(type = FieldType.Keyword)
    var likeAmount: Long = 0

    @Field(type = FieldType.Keyword, index = false)
    var width: Long = 0;

    @Field(type = FieldType.Keyword, index = false)
    var height: Long = 0;

    @Field(type = FieldType.Keyword)
    var tagList: MutableList<String> = mutableListOf()

    @Field(type = FieldType.Keyword)
    var createDate: Date = Date()

    @Field(type = FieldType.Keyword)
    var modifiedDate: Date = Date()

    constructor()

    constructor(draw: Draw, viewAmount: Long, likeAmount: Long) {
        this.id = draw.id
        this.introduction = draw.introduction
        this.url = draw.url
        this.userId = draw.userId
        this.name = draw.name
        this.privacy = draw.privacy
        this.viewAmount = viewAmount
        this.likeAmount = likeAmount
        this.width = draw.width
        this.height = draw.height
        this.createDate = draw.createDate
        this.modifiedDate = draw.modifiedDate
        this.tagList = draw.tagList.asSequence().map { it.name }.toMutableList()
    }
}