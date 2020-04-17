package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.constant.Gender
import com.junjie.secdradata.database.base.BaseEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners

/**
 * 用户
 *
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
class User() : BaseEntity(), Serializable {
    //账户id
    @Column(name = "account_id", length = 32)
    lateinit var accountId: String
    //性别
    @Column(name = "gender")
    var gender: Gender = Gender.MALE
    //生日
    @Column(name = "birthday")
    var birthday: Date = Date()
    //昵称
    @Column(name = "name")
    var name: String = "9527"
    //简介
    @Column(name = "introduction")
    var introduction: String = "大家好啊"
    //国家
    @Column(name = "country", length = 32)
    var country: String = "中国"
    //省份
    @Column(name = "province", length = 32)
    var province: String? = null
    //城市
    @Column(name = "city", length = 32)
    var city: String? = null
    //头像
    @Column(name = "head")
    var head: String? = null

    //背景图
    @Column(name = "background")
    var background: String? = null

    constructor(accountId: String, gender: Gender = Gender.MALE, birthday: Date = Date(), name: String = "9527", introduction: String = "大家好啊",
                country: String = "中国", province: String? = null, city: String? = null, head: String? = null, background: String? = null) : this() {
        this.accountId = accountId
        this.gender = gender
        this.birthday = birthday
        this.name = name
        this.introduction = introduction
        this.country = country
        this.province = province
        this.city = city
        this.head = head
        this.background = background
    }
}
