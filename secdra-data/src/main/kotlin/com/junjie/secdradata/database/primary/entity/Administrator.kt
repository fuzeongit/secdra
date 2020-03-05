package com.junjie.secdradata.database.primary.entity

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@Entity
@Table(name = "administrator", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("access_key"))])
@EntityListeners(AuditingEntityListener::class)
class Administrator {
    @Id
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null
    @Column(name = "access_key")
    lateinit var accessKey: String

    lateinit var secretKey: String
}