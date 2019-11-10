package com.junjie.secdradata.database.collect.entity

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * 账号和pixiv账号的中间表
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
class AccountToPixivUser : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var accountId: String

    lateinit var pixivUserId: String

    constructor()

    constructor(accountId: String, pixivUserId: String) {
        this.accountId = accountId
        this.pixivUserId = pixivUserId
    }
}