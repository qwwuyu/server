package com.qwwuyu.lib.ext

inline fun String?.strBlackLet(block: (String) -> Unit) {
    if (this != null && this.isNotEmpty()) {
        block(this)
    }
}

fun String?.exist(): Boolean {
    return this != null && this.isNotEmpty()
}