package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.database.base.AskEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

/**
 * 关注或粉丝消息
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "follow_message")
class FollowMessage() : AskEntity(), Serializable {
    //关注人的id
    @Column(name = "following_id", length = 32)
    lateinit var followingId: String
    // 由于read是数据库保留字
    @Column(name = "review")
    var review: Boolean = false

    constructor(followingId: String) : this() {
        this.followingId = followingId
    }
}