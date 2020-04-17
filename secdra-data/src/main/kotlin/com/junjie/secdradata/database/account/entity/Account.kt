package com.junjie.secdradata.database.account.entity

import com.junjie.secdradata.constant.UserState
import com.junjie.secdradata.database.base.BaseEntity
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.*

/**
 * 用户
 *
 * @author fjj
 */
@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("phone"))])
@EntityListeners(AuditingEntityListener::class)
class Account() : BaseEntity(), Serializable {
    //手机
    @Column(name = "phone", length = 32)
    lateinit var phone: String

    //密码
    @Column(name = "password", length = 32)
    lateinit var password: String

    @Column(name = "state")
    var state: UserState = UserState.PASS

    constructor(phone: String, password: String) : this() {
        this.phone = phone
        this.password = password
    }
}
