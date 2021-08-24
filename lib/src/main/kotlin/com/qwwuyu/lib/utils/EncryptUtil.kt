package com.qwwuyu.lib.utils

import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.security.MessageDigest

fun File.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    FileInputStream(this).use { stream ->
        val buffer = ByteArray(8192)
        var read: Int
        while (stream.read(buffer).also { read = it } > 0) {
            md.update(buffer, 0, read)
        }
    }
    return BigInteger(1, md.digest()).toString(16).toUpperCase()
}

fun String.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    md.update(this.toByteArray(Charsets.UTF_8))
    return BigInteger(1, md.digest()).toString(16).toUpperCase()
}