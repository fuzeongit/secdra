package com.junjie.secdradata.database.collect.entity

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class PixivError {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var pixivId: String

    @Column(columnDefinition = "text")
    var message: String? = null

    var record: Boolean = false

    @CreatedDate
    var createDate: Date? = null

    @LastModifiedDate
    var modifiedDate: Date? = null

    constructor()

    constructor(pixivId: String, message: String?) {
        this.pixivId = pixivId
        this.message = message
    }
}