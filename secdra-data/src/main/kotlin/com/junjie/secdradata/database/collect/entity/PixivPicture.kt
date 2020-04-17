package com.junjie.secdradata.database.collect.entity

import com.junjie.secdradata.constant.TransferState
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
@Table(name = "pixiv_picture")
class PixivPicture() : BaseEntity(), Serializable {
    @Column(name = "pixiv_id", length = 32)
    lateinit var pixivId: String
    @Column(name = "picture_id", length = 32)
    lateinit var pictureId: String
    @Column(name = "pixiv_name")
    var pixivName: String? = null
    @Column(name = "pixiv_user_name")
    var pixivUserName: String? = null
    @Column(name = "pixiv_user_id", length = 32)
    var pixivUserId: String? = null
    //竖线隔开
    @Column(name = "tag_list")
    var tagList: String? = null

    @Column(name = "state")
    var state: TransferState = TransferState.WAIT

    constructor(pixivId: String, pictureId: String) : this() {
        this.pixivId = pixivId
        this.pictureId = pictureId
    }
}