package com.qwwuyu.lib.ext

inline fun <T> Boolean.ife(trueT: () -> T, falseT: () -> T): T {
    return if (this) trueT() else falseT()
}

fun <T> Boolean.tf(t1: T, t2: T): T {
    return if (this) t1 else t2
}