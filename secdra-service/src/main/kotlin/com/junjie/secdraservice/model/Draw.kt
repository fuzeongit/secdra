package com.junjie.secdraservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.junjie.secdraservice.constant.DrawState
import com.junjie.secdraservice.constant.PrivacyState
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
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
class Draw : Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    var id: String? = null

    var userId: String? = null

    var name: String? = null

    var introduction: String? = null

    var url: String? = null

    var privacy: PrivacyState = PrivacyState.PUBLIC

    var drawState: DrawState = DrawState.PASS

    var width: Long = 0;

    var height: Long = 0;

    @JsonIgnore
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "draw_id")
    var tagList: MutableSet<Tag> = TreeSet { o1, o2 -> o1.name.compareTo(o2.name) };

    @CreatedDate
    var createDate: Date = Date()

    @LastModifiedDate
    var modifiedDate: Date = Date()
}