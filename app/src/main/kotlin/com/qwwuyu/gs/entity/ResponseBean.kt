package com.qwwuyu.gs.entity

data class ResponseBean(var state: Int = 0, var info: String? = null, var data: Any? = null) {
    fun info(info: String?): ResponseBean {
        this.info = info
        return this
    }

    fun data(data: Any?): ResponseBean {
        this.data = data
        return this
    }
}
