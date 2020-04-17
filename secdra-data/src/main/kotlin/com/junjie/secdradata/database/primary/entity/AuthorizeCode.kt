package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.database.base.AskEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.*


/**
 * 特殊码表（在cookie带这个买过来的用户才能够进行特殊操作）
 *
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "authorize_code", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("code"))])
class AuthorizeCode() : AskEntity(), Serializable {
    //操作码
    @Column(name = "code", length = 32)
    lateinit var code: String

    constructor(code: String) : this() {
        this.code = code
    }
}
