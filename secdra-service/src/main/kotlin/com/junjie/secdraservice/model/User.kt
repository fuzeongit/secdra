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
    var id: String = ""

    var phone: String? = ""

    var password: String? = ""

    var name: String? = ""

    var gender: Gender = Gender.MALE

    var head: String? = ""

    var birthday: Date = Date()

    var introduction: String? = ""

    var updatePasswordDate: Date = Date()

    var userState: UserState = UserState.PASS
}
