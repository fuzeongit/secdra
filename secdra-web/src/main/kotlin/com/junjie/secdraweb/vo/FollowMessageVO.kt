package com.junjie.secdraweb.vo

import com.junjie.secdradata.database.primary.entity.FollowMessage
import org.springframework.beans.BeanUtils
import org.springframework.data.annotation.CreatedBy
import java.util.*

class FollowMessageVO {
    lateinit var id: String

    lateinit var createdBy: String

    lateinit var followingId: String

    lateinit var createDate: Date

    lateinit var follower: UserVO

    constructor()

    constructor(followMessage: FollowMessage) {
        BeanUtils.copyProperties(followMessage, this)
    }

    constructor(followMessage: FollowMessage, follower: UserVO) {
        BeanUtils.copyProperties(followMessage, this)
        this.follower = follower
    }
}