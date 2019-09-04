package com.junjie.secdradata.database.account.entity

import com.junjie.secdradata.constant.UserState
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
class Account : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var phone: String

    lateinit var password: String

    var rePasswordDate: Date = Date()

    var userState: UserState = UserState.PASS

    @CreatedDate
    var createDate: Date? = null

    @LastModifiedDate
    var modifiedDate: Date? = null

    constructor()

    constructor(phone: String, password: String, rePasswordDate: Date = Date()) {
        this.phone = phone
        this.password = password
        this.rePasswordDate = rePasswordDate
    }
}
