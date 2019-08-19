package com.junjie.secdraservice.model

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * 回复消息
 * @author fjj
 */
@Entity
class ReplyMessage {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null
    //评论id
    lateinit var commentId: String
    //回复id
    lateinit var replyId: String
    //图片作者id
    lateinit var authorId: String
    //图片id
    lateinit var drawId: String
    //评论人id
    lateinit var criticId: String
    //回答者id
    lateinit var answererId: String

    lateinit var content: String

    var isRead: Boolean = false

    @CreatedDate
    var createDate: Date? = null

    @LastModifiedDate
    var modifiedDate: Date? = null

    constructor()

    constructor(commentId: String, replyId: String, authorId: String, drawId: String, criticId: String, answererId: String, content: String) {
        this.commentId = commentId
        this.replyId = replyId
        this.authorId = authorId
        this.drawId = drawId
        this.criticId = criticId
        this.answererId = answererId
        this.content = content
    }
}