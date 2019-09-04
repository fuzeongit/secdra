package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.constant.Gender
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
@EntityListeners(AuditingEntityListener::class)
class User : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var accountId: String

    var gender: Gender = Gender.MALE

    var birthday: Date = Date()

    var name: String = "9527"

    var introduction: String = "大家好啊"

    var address: String? = null

    var head: String? = null

    var background: String? = null

    @CreatedDate
    var createDate: Date? = null

    @LastModifiedDate
    var modifiedDate: Date? = null

    constructor()

    constructor(accountId: String, gender: Gender = Gender.MALE, birthday: Date = Date(), name: String = "9527", introduction: String = "大家好啊", address: String?, head: String?, background: String?) {
        this.accountId = accountId
        this.gender = gender
        this.birthday = birthday
        this.name = name
        this.introduction = introduction
        this.address = address
        this.head = head
        this.background = background
    }
}
