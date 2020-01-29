package com.junjie.secdraweb.vo

import com.junjie.secdradata.constant.CollectState
import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdradata.index.primary.document.PictureDocument
import org.springframework.beans.BeanUtils
import java.util.*

class PictureVO {
    lateinit var id: String

    lateinit var introduction: String

    lateinit var url: String

    lateinit var name: String

    var privacy: PrivacyState = PrivacyState.PUBLIC

    var focus: CollectState = CollectState.STRANGE

    var viewAmount: Long = 0

    var likeAmount: Long = 0

    var width: Long = 0

    var height: Long = 0

    /**
     * 由于es的查询的原因，在空的情况下也有一条空的数据而不是空数组
     */
    var tagList: List<String> = mutableListOf()
        get() {
            return field.filter { it.isNotEmpty() }
        }
    lateinit var user: UserVO

    lateinit var createDate: Date

    constructor()

    constructor(picture: PictureDocument) {
        BeanUtils.copyProperties(picture, this)
    }
}