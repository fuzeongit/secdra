package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.database.base.AskEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table


/**
 * 反馈消息
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "feedback")
class Feedback() : AskEntity(), Serializable {
    //内容
    @Column(name = "content", columnDefinition = "text")
    lateinit var content: String
    //邮箱
    @Column(name = "email", length = 32)
    var email: String? = null

    constructor(content: String, email: String?) : this() {
        this.content = content
        this.email = email
    }
}