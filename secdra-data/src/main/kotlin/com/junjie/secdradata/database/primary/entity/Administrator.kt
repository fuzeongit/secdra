package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.database.base.BaseEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "administrator", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("access_key"))])
@EntityListeners(AuditingEntityListener::class)
class Administrator : BaseEntity(), Serializable {
    @Column(name = "access_key", length = 32)
    lateinit var accessKey: String
    @Column(name = "secret_key", length = 32)
    lateinit var secretKey: String
}