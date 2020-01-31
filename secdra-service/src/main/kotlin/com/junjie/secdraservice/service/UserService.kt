package com.junjie.secdraservice.service

import com.junjie.secdradata.database.primary.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * 用户的服务
 *
 * @author fjj
 */
interface UserService {
    /**
     * 保存
     */
    fun save(user: User): User

    /**
     * 获取用户信息
     */
    fun get(id: String): User

    /**
     * 根据账户id获取用户信息
     */
    fun getByAccountId(accountId: String): User

    /**
     * 获取全部用户
     */
    fun list(name: String? = null): List<User>

    fun paging(pageable: Pageable, name: String?, accountIdList: List<String>): Page<User>
}
