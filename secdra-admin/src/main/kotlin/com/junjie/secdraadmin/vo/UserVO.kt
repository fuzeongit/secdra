package com.junjie.secdraadmin.vo

import com.junjie.secdradata.database.primary.entity.User

class UserListVO(user: User, var phone: String) {
    var id: String = user.id!!

    var accountId: String = user.accountId

    var name: String = user.name
}