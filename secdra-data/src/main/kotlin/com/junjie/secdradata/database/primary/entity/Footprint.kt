package com.junjie.secdradata.database.primary.entity

import com.junjie.secdradata.database.base.AskEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import javax.persistence.*

/**
 * 足迹
 * @author fjj
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "footprint", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("created_by", "picture_id"))])
class Footprint() : AskEntity(), Serializable {
    //图片id
    @Column(name = "picture_id", length = 32)
    lateinit var pictureId: String

    constructor(pictureId: String) : this() {
        this.pictureId = pictureId
    }
}