package com.junjie.secdraweb.vo

import com.junjie.secdradata.database.primary.entity.ReplyMessage
import org.springframework.beans.BeanUtils
import java.util.*

class ReplyMessageVO(replyMessage: ReplyMessage, var answerer: UserVO) {
    lateinit var id: String
    //评论id
    lateinit var commentId: String
    //回复id
    lateinit var replyId: String
    //图片作者id
    lateinit var authorId: String
    //图片id
    lateinit var pictureId: String
    //评论人id
    lateinit var criticId: String
    //回答者id
    lateinit var createdBy: String

    lateinit var content: String

    lateinit var createDate: Date

    init {
        BeanUtils.copyProperties(replyMessage, this)
    }
}