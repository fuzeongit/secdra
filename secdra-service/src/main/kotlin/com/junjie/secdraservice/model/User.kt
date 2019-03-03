package com.junjie.secdraservice.model

import com.junjie.secdraservice.contant.Gender
import com.junjie.secdraservice.contant.UserState
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
@Table(uniqueConstraints= [UniqueConstraint(columnNames = arrayOf("phone"))])
class User : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    var phone: String? = null

    var password: String? = null

    var name: String? = null

    var gender: Gender = Gender.MALE

    var head: String? = null

    var birthday: Date? = Date()

    var introduction: String? = null

    var address: String? = null

    var background: String? = null

    var rePasswordDate: Date? = Date()

    var userState: UserState = UserState.PASS

    @CreatedDate
    var createDate: Date = Date()

    @LastModifiedDate
    var modifiedDate: Date = Date()
}
