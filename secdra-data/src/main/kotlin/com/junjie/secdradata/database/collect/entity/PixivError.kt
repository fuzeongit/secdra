package com.junjie.secdradata.database.collect.entity

import com.junjie.secdradata.database.base.BaseEntity
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "pixiv_error")
class PixivError() : BaseEntity(), Serializable {
    @Column(name = "pixiv_id", length = 32)
    lateinit var pixivId: String

    @Column(name = "status")
    var status: Int? = null

    @Column(columnDefinition = "text")
    var message: String? = null

    @Column(name = "record")
    var record: Boolean = false

    constructor(pixivId: String, status: Int, message: String?) : this() {
        this.pixivId = pixivId
        this.status = status
        this.message = message
    }
}