package com.junjie.secdraservice.model

import com.junjie.secdraservice.contant.Gender
import com.junjie.secdraservice.contant.UserState
import org.hibernate.annotations.GenericGenerator

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import java.util.Date

/**
 * 用户
 *
 * @author fjj
 */
@Entity
class User : Base() {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    var phone: String? = null

    var password: String? = null

    var name: String? = null

    var gender: Gender? = null

    var head: String? = null

    var birthday: Date? = null

    var introduction: String? = null

    var updatePasswordDate: Date? = null

    var userState: UserState? = null
}
