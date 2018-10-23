package com.junjie.secdraservice.model

import com.junjie.secdraservice.contant.DrawState
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Draw : Base() {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String = ""

    var introduction: String? = ""

    var url: String? = ""

    var userId: String = ""

    var isPrivate: Boolean = false

    var addTime : Date = Date()

    var drawState: DrawState = DrawState.PASS

    var viewAmount: Int = 0

    var likeAmount: Int = 0
}