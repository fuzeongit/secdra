package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.database.base.AskEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.*

/**
 * 通知信息设置
 * @author fjj
 */
@Entity
@Table(name = "message_settings", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("created_by"))])
@EntityListeners(AuditingEntityListener::class)
class MessageSettings() : AskEntity(), Serializable {
    //评论状态
    @Column(name = "comment_status")
    var commentStatus: Boolean = true
    //回复状态
    @Column(name = "reply_status")
    var replyStatus: Boolean = true
    //关注状态
    @Column(name = "follow_status")
    var followStatus: Boolean = true
    //系统通知状态
    @Column(name = "system_status")
    var systemStatus: Boolean = true
}