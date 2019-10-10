package com.junjie.secdradata.database.primary.entity

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id


/**
 * 反馈消息
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
class Feedback {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null
    //评论id
    lateinit var content: String
    //图片作者id
    var email: String? = null

    @CreatedDate
    var createDate: Date? = null

    constructor()

    constructor(content: String, email: String?) {
        this.content = content
        this.email = email
    }
}