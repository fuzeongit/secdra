package com.junjie.secdraservice.model

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * 评论
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
class Comment : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null
    //图片作者id
    lateinit var authorId: String
    //评论人id
    lateinit var criticId: String

    lateinit var drawId: String

    lateinit var content: String

    @CreatedDate
    var createDate: Date? = null

    constructor()

    constructor(authorId: String, criticId: String, drawId: String, content: String) {
        this.authorId = authorId
        this.criticId = criticId
        this.drawId = drawId
        this.content = content
    }
}