package com.qwwuyu.lib

import kotlinx.coroutines.runBlocking

fun main() {
    println("kt123")
}

object WithConfiguration {
    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking {
        println("kt1234")
    }
}