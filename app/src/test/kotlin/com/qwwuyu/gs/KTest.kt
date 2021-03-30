package com.qwwuyu.gs

import com.qwwuyu.lib.utils.SystemUtils
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset

class KTest {
    @Test
    fun test() {
        val builder = ProcessBuilder("wget", "https://eternallybored.org/misc/wget/releases/old/wget-1.20.3-win64.zip")
        builder.directory(File("D:\\1-project\\2-github"))
        builder.redirectErrorStream(true)
        val pro = builder.start()
        Thread {
            Thread.sleep(10000)
            pro.destroy()
        }.start()
        val inputStream = pro.inputStream
        println()
        println()
        println("===========================")
        print(streamToString(inputStream, "GBK"))
        println("===========================")

    }

    private fun streamToString(ins: InputStream, charsetName: String): String {
        val stream = ByteArrayOutputStream()
        val bs = ByteArray(1024)
        var read: Int
        while (ins.read(bs).also { read = it } != -1) {
            stream.write(bs, 0, read)
            val bs2 = ByteArray(read)
            System.arraycopy(bs, 0, bs2, 0, bs2.size)
            println()
            println(String(bs2, Charset.forName(charsetName)))
            if (stream.size() > 1024 * 100) {
                return String(stream.toByteArray(), Charset.forName(charsetName)) + SystemUtils.LINE_SEPARATOR + "数据多于100kb"
            }
        }
        return String(stream.toByteArray(), Charset.forName(charsetName))
    }
}