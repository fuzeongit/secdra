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
 * 评论消息
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
class CommentMessage {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null
    //评论id
    lateinit var commentId: String
    //图片作者id
    lateinit var authorId: String
    //图片id
    lateinit var drawId: String
    //评论人id
    lateinit var criticId: String

    lateinit var content: String

    var read: Boolean = false

    @CreatedDate
    var createDate: Date? = null

    constructor()

    constructor(commentId: String, authorId: String, drawId: String, criticId: String, content: String) {
        this.commentId = commentId
        this.authorId = authorId
        this.drawId = drawId
        this.criticId = criticId
        this.content = content
    }
}