package com.junjie.secdradata.database.primary.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.*

/**
 * 图片标签
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
class Tag : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var name: String

    @JsonIgnore
    @ManyToOne
    var draw: Draw? = null

    @CreatedDate
    var createDate: Date? = null

    constructor()

    constructor(name: String) {
        this.name = name
    }
}