package com.junjie.secdraservice.model

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * 回复
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
class Reply {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null
    //评论id
    lateinit var commentId: String
    //图片作者id
    lateinit var authorId: String
    //评论人id
    lateinit var criticId: String
    //回答者id
    lateinit var answererId: String
    //图片id
    lateinit var drawId: String

    lateinit var content: String

    @CreatedDate
    var createDate: Date? = null

    constructor()

    constructor(commentId: String, authorId: String, criticId: String, answererId: String, drawId: String, content: String) {
        this.commentId = commentId
        this.authorId = authorId
        this.criticId = criticId
        this.answererId = answererId
        this.drawId = drawId
        this.content = content
    }
}