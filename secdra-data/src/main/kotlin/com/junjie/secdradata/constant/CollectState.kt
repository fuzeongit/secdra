package com.junjie.secdradata.constant

enum class CollectState private constructor(var value: String) {
    STRANGE("未关注"),
    CONCERNED("已关注"),
    SElF("自己")
}