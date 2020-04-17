package com.junjie.secdradata.database.collect.entity

import com.junjie.secdradata.database.base.BaseEntity
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.*

/**
 * 账号和pixiv账号的中间表
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "account_to_pixiv_user")
class AccountToPixivUser() : BaseEntity(), Serializable {
    @Column(name = "account_Id", length = 32)
    lateinit var accountId: String

    @Column(name = "pixiv_user_id", length = 32)
    lateinit var pixivUserId: String

    constructor(accountId: String, pixivUserId: String) : this() {
        this.accountId = accountId
        this.pixivUserId = pixivUserId
    }
}