package com.junjie.secdraservice.model

import com.junjie.secdraservice.constant.Gender
import com.junjie.secdraservice.constant.UserState
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
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
class User : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var phone: String

    lateinit var password: String

    var gender: Gender = Gender.MALE

    var birthday: Date = Date()

    var name: String = "9527"

    var introduction: String = "大家好啊"

    var address: String? = null

    var head: String? = null

    var background: String? = null

    var rePasswordDate: Date = Date()

    var userState: UserState = UserState.PASS

    @CreatedDate
    var createDate: Date = Date()

    @LastModifiedDate
    var modifiedDate: Date = Date()

    constructor()

    constructor(phone: String, password: String, rePasswordDate: Date = Date()) {
        this.phone = phone
        this.password = password
        this.rePasswordDate = rePasswordDate
    }

    constructor(phone: String, password: String, gender: Gender = Gender.MALE, birthday: Date = Date(), name: String = "9527", introduction: String = "大家好啊", address: String?, head: String?, background: String?) {
        this.phone = phone
        this.password = password
        this.gender = gender
        this.birthday = birthday
        this.name = name
        this.introduction = introduction
        this.address = address
        this.head = head
        this.background = background
    }
}
