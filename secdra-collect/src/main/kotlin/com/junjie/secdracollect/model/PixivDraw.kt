package com.junjie.secdracollect.model

import com.junjie.secdracollect.constant.TransferState
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id


@Entity
@EntityListeners(AuditingEntityListener::class)
class PixivDraw : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var pixivId: String

    lateinit var drawId: String

    var pixivName: String? = null

    var pixivUserName: String? = null

    var pixivUserId: String? = null

    var tagList: String? = null

    var state: TransferState = TransferState.WAIT

    @CreatedDate
    var createDate: Date? = null

    @LastModifiedDate
    var modifiedDate: Date? = null

    constructor()

    constructor(pixivId: String, drawId: String) {
        this.pixivId = pixivId
        this.drawId = drawId
    }
}