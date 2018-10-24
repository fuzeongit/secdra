package com.junjie.secdraservice.model

import com.junjie.secdraservice.contant.DrawState
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Draw {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    var introduction: String? = null

    var url: String? = null

    var userId: String? = null

    var isPrivate: Boolean = false

    var addTime: Date? = null

    var drawState: DrawState? = null

    var viewAmount: Int = 0

    var likeAmount: Int = 0

    @CreatedDate
    var createDate: Date = Date()

    @LastModifiedDate
    var modifiedDate: Date = Date()
}