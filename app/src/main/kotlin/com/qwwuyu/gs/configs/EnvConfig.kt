package com.qwwuyu.gs.configs

import com.qwwuyu.lib.ext.tf
import com.qwwuyu.lib.utils.CommUtil
import org.springframework.core.env.Environment
import java.io.File

class EnvConfig(env: Environment) {
    companion object {
        lateinit var instance: EnvConfig
        private fun isInit() = ::instance.isInitialized
    }

    val privateKey = env.getProperty("env.private-key")
    val publicKey = env.getProperty("env.public-key")
    val fileDir = File(env.getProperty(CommUtil.isWindows().tf("env.file-dir-win", "env.file-dir-linux"))!!)
    val javaDir = File(env.getProperty(CommUtil.isWindows().tf("env.java-dir-win", "env.java-dir-linux"))!!)

    init {
        if (!fileDir.isDirectory && !fileDir.mkdirs()) {
            throw RuntimeException("创建文件目录失败")
        }
        if (!javaDir.isDirectory && !javaDir.mkdirs()) {
            throw RuntimeException("创建java目录失败")
        }
        instance = if (isInit()) throw RuntimeException("EnvConfig创建多次") else this
    }
}