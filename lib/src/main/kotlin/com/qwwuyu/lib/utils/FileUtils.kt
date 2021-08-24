package com.qwwuyu.lib.utils

import java.io.File
import java.lang.Exception

object FileUtils {
    /* ======================== other ======================== */
    fun webInfFile(path: String, name: String): File {
        val classesPath = FileUtils::class.java.classLoader.getResource("/").path
        val file = File(File(File(classesPath).parentFile, path), name)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        return file
    }

    /* ======================== util ======================== */
    @Throws(Exception::class)
    fun isChild(parent: File, child: File): Boolean {
        val parentFile = parent.canonicalFile
        val childFile = child.canonicalFile
        return if (parentFile.parent == null && childFile.parent == null) {
            parentFile.path == childFile.path
        } else if (parentFile.parent == null) {
            childFile.path.startsWith(parentFile.path)
        } else if (parentFile.parent == childFile.parent) {
            parentFile.path == childFile.path
        } else {
            childFile.path.startsWith(parentFile.path)
        }
    }

    fun delChildFile(file: File) {
        if (!file.isDirectory) return
        file.listFiles()?.let { files ->
            files.forEach { file ->
                file.delete()
            }
        }
    }

    fun isFile(file: File): Boolean {
        return exists(file) && file.isFile
    }

    fun isDirectory(file: File): Boolean {
        return exists(file) && file.isDirectory
    }

    @JvmStatic
    fun exists(file: File?): Boolean {
        return file != null && file.exists()
    }

    fun getFileSize(size: Long): String {
        return when {
            size < 1024L -> size.toString() + "B"
            size < 1024L * 1024 -> String.format("%.2fKB", size / 1024f)
            size < 1024L * 1024 * 1024 -> String.format("%.2fMB", size / 1024 / 1024f)
            else -> String.format("%.2fGB", size / 1024 / 1024 / 1024f)
        }
    }
}