package com.qwwuyu.gs.helper

import com.qwwuyu.lib.utils.CommUtil
import com.qwwuyu.lib.utils.FileUtils
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.net.URLDecoder

object FileDownloadHelper {
    const val DOWNLOADING = 0
    const val DOWNLOAD_OK = 1
    const val DOWNLOAD_ERROR = 2

    private var thread: DownloadThread? = null
    var progress: DownloadProgress? = null
        private set

    fun download(con: URLConnection, file: File, close: () -> Unit): Boolean {
        val total = con.contentLength
        synchronized(FileDownloadHelper::class.java) {
            if (thread == null) {
                progress = DownloadProgress(total)
                thread = DownloadThread(con, file, close, progress!!)
                thread!!.start()
            } else {
                return false
            }
        }
        return true
    }

    fun cancelDownload() {
        synchronized(FileDownloadHelper::class.java) { thread?.shutdown() }
    }

    fun getNameForDisposition(con: URLConnection, url: URL): String {
        val fieldValue = con.getHeaderField("Content-Disposition")
        var filename: String? = null
        if (fieldValue != null) {
            filename = fieldValue.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$".toRegex(), "$1")
        }
        if (CommUtil.isEmpty(filename)) {
            filename = FilenameUtils.getName(url.path)
        }
        filename = URLDecoder.decode(filename, "UTF-8")
        return filename!!
    }

    private fun end(progress: DownloadProgress, close: () -> Unit, e: Exception?) {
        progress.state = if (e == null) 1 else 2
        progress.e = e
        synchronized(FileDownloadHelper::class.java) { thread = null }
        close.invoke()
    }

    private class DownloadThread(private val con: URLConnection, private val file: File, private val close: () -> Unit, private val progress: DownloadProgress) : Thread() {
        private var inputStream: InputStream? = null

        override fun run() {
            try {
                FileOutputStream(file).use { outputStream ->
                    con.getInputStream().use { inputStream ->
                        this.inputStream = inputStream
                        val bytes = ByteArray(100 * 1024)
                        var read: Int
                        while (inputStream.read(bytes).also { read = it } != -1) {
                            outputStream.write(bytes, 0, read)
                            progress.progress += read
                        }
                        this.inputStream = null
                        end(progress, close, null)
                    }
                }
            } catch (e: Exception) {
                inputStream = null
                end(progress, close, e)
            }
        }

        fun shutdown() {
            inputStream?.let { CommUtil.closeStream(it) }
        }
    }

    class DownloadProgress(var total: Int) {
        var progress = 0
        var state = DOWNLOADING
        var e: Exception? = null

        fun progressText(): String {
            return FileUtils.getFileSize(progress.toLong()) + "/" + if (total > 0) FileUtils.getFileSize(total.toLong()) else "*"
        }

        fun errorText(): String {
            return e?.message ?: ""
        }
    }
}