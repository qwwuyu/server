package com.qwwuyu.gs.entity

data class User(
        var id: Int? = -1,
        var name: String? = null, var pwd: String? = null, var nick: String? = null,
        var auth: Int? = -1, var ip: String? = null,
        var token: String? = null, var apptoken: String? = null,
        var time: Long? = null, var apptime: Long? = null,
)
