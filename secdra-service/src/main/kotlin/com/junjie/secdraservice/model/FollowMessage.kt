package com.junjie.secdraservice.model

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
 * 关注或粉丝消息
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
class FollowMessage {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var followerId: String

    lateinit var followingId: String

    var read: Boolean = false

    @CreatedDate
    var createDate: Date? = null

    constructor()

    constructor(followerId: String, followingId: String) {
        this.followerId = followerId
        this.followingId = followingId
    }
}