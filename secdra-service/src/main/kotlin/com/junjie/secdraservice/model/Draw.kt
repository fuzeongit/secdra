package com.junjie.secdraservice.model

import com.junjie.secdraservice.contant.DrawState
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*

@Entity
@NamedEntityGraph(name = "Draw.Tag", attributeNodes = [NamedAttributeNode("tagList")])
class Draw {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    var introduction: String? = null

    var url: String? = null

    var userId: String? = null

    var name: String? = null

    var isPrivate: Boolean = false

    var drawState: DrawState = DrawState.PASS

    var viewAmount: Int = 0

    var likeAmount: Int = 0

    var width: Int = 0;

    var height: Int = 0;

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "draw_id")
    var tagList: MutableSet<Tag>? = null

    @CreatedDate
    var createDate: Date = Date()

    @LastModifiedDate
    var modifiedDate: Date = Date()
}