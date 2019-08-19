package com.junjie.secdraservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.junjie.secdraservice.constant.DrawState
import com.junjie.secdraservice.constant.PrivacyState
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.*
import java.util.TreeSet

/**
 * 画册
 * @author fjj
 */
@Entity
@NamedEntityGraph(name = "Draw.Tag", attributeNodes = [NamedAttributeNode("tagList")])
@Table(uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("url"))])
@EntityListeners(AuditingEntityListener::class)
class Draw : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var userId: String

    lateinit var url: String

    var name: String = "无题"

    var introduction: String = "身无彩凤双飞翼，心有灵犀一点通"

    var privacy: PrivacyState = PrivacyState.PUBLIC

    var drawState: DrawState = DrawState.PASS

    var width: Long = 0;

    var height: Long = 0;

    @JsonIgnore
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "draw_id")
    var tagList: MutableSet<Tag> = TreeSet { o1, o2 -> o1.name.compareTo(o2.name) };

    @CreatedDate
    var createDate: Date? = null

    @LastModifiedDate
    var modifiedDate: Date? = null

    constructor()

    constructor(userId: String, url: String, name: String = "无题", introduction: String = "身无彩凤双飞翼，心有灵犀一点通") {
        this.userId = userId
        this.url = url
        this.name = name
        this.introduction = introduction
    }

    constructor(userId: String, url: String, width: Long, height: Long, name: String = "无题", introduction: String = "身无彩凤双飞翼，心有灵犀一点通") {
        this.userId = userId
        this.url = url
        this.name = name
        this.introduction = introduction
        this.width = width
        this.height = height
    }
}