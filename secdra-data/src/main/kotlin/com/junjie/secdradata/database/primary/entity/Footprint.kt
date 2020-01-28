package com.junjie.secdradata.database.primary.entity

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
 * 足迹
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
class Footprint {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var userId: String

    lateinit var pictureId: String

    @CreatedDate
    var createDate: Date? = null

    @LastModifiedDate
    var modifiedDate: Date? = null

    constructor()

    constructor(userId: String, pictureId: String) {
        this.userId = userId
        this.pictureId = pictureId
    }
}