package com.qwwuyu.gs.configs

import com.qwwuyu.lib.utils.FileUtils
import org.springframework.core.env.Environment
import java.io.File

/**
 * 环境变量
 */
class EnvConfig(env: Environment) {
    companion object {
        lateinit var instance: EnvConfig
        private fun isInit() = ::instance.isInitialized
    }

    val privateKey = env.getProperty("env.private-key")!!
    val publicKey = env.getProperty("env.public-key")!!
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