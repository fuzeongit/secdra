package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.constant.Gender
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.*


/**
 * 特殊码表（在cookie带这个买过来的用户才能够进行特殊操作）
 *
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("code"))])
class SpecialCode : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var code: String

    @CreatedDate
    var createDate: Date? = null

    constructor()

    constructor(code: String) {
        this.code = code
    }
}
