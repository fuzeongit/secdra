package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.database.base.AskEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

/**
 * 回复
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "reply")
class Reply() : AskEntity(), Serializable {
    //评论id
    @Column(name = "comment_id", length = 32)
    lateinit var commentId: String
    //图片作者id
    @Column(name = "author_id", length = 32)
    lateinit var authorId: String
    //评论人id
    @Column(name = "critic_id", length = 32)
    lateinit var criticId: String
    //图片id
    @Column(name = "picture_id", length = 32)
    lateinit var pictureId: String
    //内容
    @Column(name = "content")
    lateinit var content: String

    constructor(commentId: String, authorId: String, criticId: String, pictureId: String, content: String) : this() {
        this.commentId = commentId
        this.authorId = authorId
        this.criticId = criticId
        this.pictureId = pictureId
        this.content = content
    }
}