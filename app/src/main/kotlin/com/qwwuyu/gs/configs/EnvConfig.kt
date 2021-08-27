package com.qwwuyu.gs.configs

import com.qwwuyu.lib.utils.FileUtils
import java.io.File

/**
 * 环境变量
 */
class EnvConfig(envProperties: EnvProperties) {
    companion object {
        lateinit var instance: EnvConfig
        private fun isInit() = ::instance.isInitialized
    }

    val privateKey = envProperties.privateKey
    val publicKey = envProperties.publicKey
    val fileDir = File(Constant.SYSTEM_PATH)
    val javaDir = File(Constant.JAVA_PATH)
    val tmpDir = File(Constant.TMP_PATH)

    init {
        if (!fileDir.isDirectory && !fileDir.mkdirs()) {
            throw RuntimeException("创建文件目录失败")
        }
        if (!javaDir.isDirectory && !javaDir.mkdirs()) {
            throw RuntimeException("创建java目录失败")
        }
        FileUtils.delChildFile(tmpDir)
        instance = if (isInit()) throw RuntimeException("EnvConfig创建多次") else this
    }
}