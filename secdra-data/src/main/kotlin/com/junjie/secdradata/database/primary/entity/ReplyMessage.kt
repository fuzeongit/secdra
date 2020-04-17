package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.database.base.AskEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.*

/**
 * 回复消息
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "reply_message", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("reply_id"))])
class ReplyMessage() : AskEntity(), Serializable {
    //评论id
    @Column(name = "comment_id", length = 32)
    lateinit var commentId: String
    //回复id
    @Column(name = "reply_id", length = 32)
    lateinit var replyId: String
    //图片作者id
    @Column(name = "author_id", length = 32)
    lateinit var authorId: String
    //图片id
    @Column(name = "picture_id", length = 32)
    lateinit var pictureId: String
    //评论人id
    @Column(name = "critic_id", length = 32)
    lateinit var criticId: String
    //内容
    @Column(name = "content")
    lateinit var content: String
    // 由于read是数据库保留字
    var review: Boolean = false

    constructor(commentId: String, replyId: String, authorId: String, pictureId: String, criticId: String, content: String) : this() {
        this.commentId = commentId
        this.replyId = replyId
        this.authorId = authorId
        this.pictureId = pictureId
        this.criticId = criticId
        this.content = content
    }
}