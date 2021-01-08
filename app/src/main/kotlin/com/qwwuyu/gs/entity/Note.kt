package com.qwwuyu.gs.entity

data class Note(var id: Int? = -1, var userId: Int? = -1,
                var nick: String? = null, var title: String? = null, var content: String? = null,
                var time: Long? = null)
