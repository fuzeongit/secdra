package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Follower
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * 用户的关注服务
 *
 * @author fjj
 */

interface IFollowerService {
    fun exists(userId: String?, followerId: String): Boolean?

    fun save(userId: String, followerId: String): Follower

    fun remove(userId: String, followerId: String): Boolean

    fun paging(userId: String, pageable: Pageable): Page<Follower>
}