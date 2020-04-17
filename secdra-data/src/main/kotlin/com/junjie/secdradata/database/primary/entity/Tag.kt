package com.junjie.secdradata.database.primary.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.junjie.secdradata.database.base.AskEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.*

/**
 * 图片标签
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "tag")
class Tag() : AskEntity(), Serializable {
    //名称
    @Column(name = "name")
    lateinit var name: String

    @JsonIgnore
    @ManyToOne
    var picture: Picture? = null

    constructor(name: String) : this() {
        this.name = name
    }
}