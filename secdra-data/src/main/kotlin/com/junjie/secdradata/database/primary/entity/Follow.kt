package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.database.base.AskEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.*

/**
 * 关注或粉丝
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "follow", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("created_by", "following_id"))])
class Follow() : AskEntity(), Serializable {
    //关注人的id
    @Column(name = "following_id", length = 32)
    lateinit var followingId: String

    constructor(followingId: String) : this() {
        this.followingId = followingId
    }
}