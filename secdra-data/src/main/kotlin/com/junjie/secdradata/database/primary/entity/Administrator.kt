package com.junjie.secdradata.database.primary.entity

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("accessKey"))])
@EntityListeners(AuditingEntityListener::class)
class Administrator {
    @Id
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var accessKey: String

    lateinit var secretKey: String
}