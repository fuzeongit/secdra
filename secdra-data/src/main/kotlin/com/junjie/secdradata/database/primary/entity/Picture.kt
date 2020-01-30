package com.junjie.secdradata.database.primary.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.junjie.secdradata.constant.PictureLifeState
import com.junjie.secdradata.constant.PictureState
import com.junjie.secdradata.constant.PrivacyState
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
@NamedEntityGraph(name = "Picture.Tag", attributeNodes = [NamedAttributeNode("tagList")])
@Table(uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("url"))])
@EntityListeners(AuditingEntityListener::class)
class Picture : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    lateinit var url: String

    var name: String = "无题"

    var introduction: String = "身无彩凤双飞翼，心有灵犀一点通"

    var privacy: PrivacyState = PrivacyState.PUBLIC

    var pictureState: PictureState = PictureState.PASS
    //是否存在，不存在不建立索引
    var life: PictureLifeState = PictureLifeState.EXIST

    var width: Long = 0

    var height: Long = 0

    @JsonIgnore
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "picture_id")
    var tagList: MutableSet<Tag> = TreeSet { o1, o2 -> o1.name.compareTo(o2.name) }

    @CreatedDate
    var createDate: Date? = null

    @LastModifiedDate
    var modifiedDate: Date? = null

    /**
     * 用户id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    lateinit var user: User

    constructor()

    constructor(user: User, url: String, name: String = "无题", introduction: String = "身无彩凤双飞翼，心有灵犀一点通") {
        this.user = user
        this.url = url
        this.name = name
        this.introduction = introduction
    }

    constructor(user: User, url: String, width: Long, height: Long, name: String = "无题", introduction: String = "身无彩凤双飞翼，心有灵犀一点通") {
        this.user = user
        this.url = url
        this.name = name
        this.introduction = introduction
        this.width = width
        this.height = height
    }
}