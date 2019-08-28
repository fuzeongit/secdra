package com.junjie.secdradata.constant

enum class TransferState(var value: String) {
    WAIT("待处理"),
    SUCCESS("已完成"),
    FAIL("处理失败"),
    ABANDON("放弃处理")
}