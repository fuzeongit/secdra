package com.junjie.secdraservice.model

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.util.*
import javax.persistence.*

/**
 * 通知信息设置
 * @author fjj
 */
@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("userId"))])
class MessageSettings : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var userId: String

    var commentStatus: Boolean = true

    var replyStatus: Boolean = true

    var followStatus: Boolean = true

    var systemStatus: Boolean = true

    @CreatedDate
    var createDate: Date? = null

    @LastModifiedDate
    var modifiedDate: Date? = null

    constructor()

    constructor(userId: String) {
        this.userId = userId
    }
}