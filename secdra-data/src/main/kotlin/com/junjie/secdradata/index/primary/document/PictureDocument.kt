package com.junjie.secdradata.index.primary.document

import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdradata.constant.SizeType
import com.junjie.secdradata.database.primary.entity.Picture
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.io.Serializable
import java.util.*

@Document(indexName = "index_picture_search")
class PictureDocument : Serializable {
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
    var width: Long = 0

    @Field(type = FieldType.Keyword, index = false)
    var height: Long = 0

    @Field(type = FieldType.Keyword, index = false)
    lateinit var sizeType: SizeType

    @Field(type = FieldType.Keyword)
    var tagList: MutableList<String> = mutableListOf()

    @Field(type = FieldType.Keyword)
    var createDate: Date = Date()

    @Field(type = FieldType.Keyword)
    var modifiedDate: Date = Date()

    constructor()

    constructor(picture: Picture) {
        this.id = picture.id
        this.introduction = picture.introduction
        this.url = picture.url
        this.userId = picture.user.id!!
        this.name = picture.name
        this.privacy = picture.privacy
        this.width = picture.width
        this.height = picture.height
        this.sizeType = picture.sizeType
        this.createDate = picture.createDate!!
        this.modifiedDate = picture.modifiedDate!!
        this.tagList = picture.tagList.asSequence().map { it.name }.toMutableList()
    }
}