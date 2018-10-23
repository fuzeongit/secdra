package com.junjie.secdracore.exception

class ProgramException : Exception {
    var status: Int = 0

    constructor(message: String, status: Int) : super(message) {
        this.status = status
    }

    constructor(message: String) : super(message) {
        this.status = 500
    }
}
