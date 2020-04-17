package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.database.base.AskEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

/**
 * 评论
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "comment")
class Comment() : AskEntity(), Serializable {
    //图片作者id
    @Column(name = "author_id", length = 32)
    lateinit var authorId: String
    //图片id
    @Column(name = "picture_id", length = 32)
    lateinit var pictureId: String
    //内容
    @Column(name = "content", length = 32)
    lateinit var content: String

    constructor(authorId: String, pictureId: String, content: String) : this() {
        this.authorId = authorId
        this.pictureId = pictureId
        this.content = content
    }
}