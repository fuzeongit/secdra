package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.database.base.BaseEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

/**
 * 系统通知消息
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "system_message")
class SystemMessage() : BaseEntity(), Serializable {
    //标题
    @Column(name = "title")
    lateinit var title: String
    //接收人id
    @Column(name = "user_id")
    lateinit var userId: String
    //内容
    @Column(columnDefinition = "text")
    lateinit var content: String

    // 是否阅读，由于read是数据库保留字
    @Column(name = "review")
    var review: Boolean = false


    constructor(userId: String, title: String, content: String) : this() {
        this.userId = userId
        this.title = title
        this.content = content
    }
}