package com.junjie.secdracore.constant

/**
 * redis常量
 * @author fjj
 */
interface RedisConstant {
    companion object {

        const val TOKEN_PREFIX = "token:%s"

        const val OPENID_PREFIX = "openid:%s"

        const val EXPIRE = 7200 //2小时
    }
}
