package com.junjie.secdracollect.model

import com.junjie.secdracollect.constant.TransferState
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id


@Entity
class PixivDraw : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var pixivId: String

    var pixivName: String? = null

    var pixivUserName: String? = null

    var pixivUserId: String? = null

    var tagList: String? = null

    var state: TransferState = TransferState.WAIT

    var drawId: String? = null

    @CreatedDate
    var createDate: Date = Date()

    @LastModifiedDate
    var modifiedDate: Date = Date()
}