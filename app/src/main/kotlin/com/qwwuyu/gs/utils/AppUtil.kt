package com.qwwuyu.gs.utils

import com.google.gson.reflect.TypeToken
import com.qwwuyu.gs.configs.Constant
import com.qwwuyu.gs.configs.EnvConfig
import com.qwwuyu.gs.entity.ResponseBean
import com.qwwuyu.gs.entity.User
import com.qwwuyu.lib.gson.GsonHelper
import com.qwwuyu.lib.utils.BCrypt
import com.qwwuyu.lib.utils.CommUtil
import com.qwwuyu.lib.utils.RSAUtil
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object AppUtil {
    fun getSuccessBean(): ResponseBean {
        return ResponseBean(Constant.HTTP_SUC, "请求成功")
    }

    fun getErrorBean(): ResponseBean {
        return ResponseBean(Constant.HTTP_ERR, "请求失败")
    }

    /** 输出JSON数据  */
    fun render(response: HttpServletResponse, bean: ResponseBean) {
        response.contentType = "text/plain;charset=utf-8"
        response.setHeader("Pragma", "No-cache")
        response.setHeader("Cache-Control", "no-cache")
        response.setDateHeader("Expires", 0)
        try {
            response.writer.write(GsonHelper.toJson(bean))
        } catch (ignored: IOException) {
        }
    }

    fun isNull(response: HttpServletResponse, vararg parame: String?): Boolean {
        for (s in parame) {
            if (null == s || "" == s) {
                render(response, getErrorBean().apply { info = "参数不正确" })
                return true
            }
        }
        return false
    }

    @JvmOverloads
    fun renderInfo(response: HttpServletResponse, info: String?, code: Int = 0): Boolean {
        if (info != null) {
            if (code > 0) response.status = code
            render(response, getErrorBean().apply { this.info = info })
            return true
        }
        return false
    }

    /* ======================== account ======================== */
    /** BCrypt固定格式加密密码  */
    fun handPwd(_acc: String, pwd: String?): String {
        var acc = _acc
        val length = acc.length
        val accBuilder = StringBuilder(acc)
        for (i in length..21) {
            accBuilder.append("0")
        }
        acc = accBuilder.toString()
        val salt = "$2a$10$" + acc.replace("_".toRegex(), "/")
        return BCrypt.hashpw(pwd, salt)
    }

    /** 生成token  */
    fun makeToken(user: User): String {
        val map: MutableMap<String, Any?> = HashMap()
        map["acc"] = user.name
        map["nick"] = user.nick
        map["auth"] = user.auth
        map["id"] = user.id
        map["uuid"] = UUID.randomUUID()
        return String(Base64.getEncoder().encode(GsonHelper.toJson(map).toByteArray(StandardCharsets.UTF_8)))
    }

    /** 解析token  */
    fun parseToken(token: String): Map<String, Any> {
        return GsonHelper.fromType(String(Base64.getDecoder().decode(token.toByteArray()), StandardCharsets.UTF_8),
                object : TypeToken<Map<String, Any>>() {}.type)!!
    }

    /** 从请求中获取token  */
    fun getToken(request: HttpServletRequest): String? {
        var token = request.getParameter("token")
        if (token.isNullOrEmpty()) {
            val cookies = request.cookies
            if (cookies != null) {
                for (cookie in cookies) {
                    if ("token" == cookie.name) {
                        token = cookie.value
                    }
                }
            }
        }
        return token
    }

    fun getUser(request: HttpServletRequest): User {
        return (request.getAttribute(Constant.KEY_USER) as User?) ?: throw RuntimeException("user is null")
    }

    /* ======================== java file ======================== */
    private var id: Long = 0

    @Synchronized
    private fun nextID(): String {
        return (id++).toString()
    }

    fun nextJavaFile(): File {
        val file = File(EnvConfig.instance.javaDir, "Code" + nextID() + ".java")
        if (!file.exists()) file.createNewFile()
        return file
    }

    /* ========================  ======================== */
    fun formatHtmlTemp() {
        val read = CommUtil.read("1.txt")
        println(read.replace("[\t\r\n]".toRegex(), "").replace("\\s+".toRegex(), " "))
    }

    fun sys(request: HttpServletRequest) {
        for ((key, value) in request.parameterMap) {
            println(key + ":" + Arrays.toString(value))
        }
    }

    /* ======================== rsa ======================== */
    var rsaUtil: RSAUtil? = null

    fun defaultRSA(): RSAUtil {
        if (rsaUtil == null) {
            synchronized(RSAUtil::class.java) {
                if (rsaUtil == null) {
                    rsaUtil = RSAUtil.loadKeyForBase64(EnvConfig.instance.privateKey, EnvConfig.instance.publicKey)
                }
            }
        }
        return rsaUtil!!
    }

    fun testRSA() {
        println(System.getProperty("java.version"))
        val privateKeyFile = File("privateKeyFile")
        val publicKeyFile = File("publicKeyFile")
        // RSAUtil rsaUtil = RSAUtil.genRSAKeyPair(privateKeyFile, publicKeyFile);
        val rsaUtil = RSAUtil.loadKeyForBase64(privateKeyFile, publicKeyFile)
        val clearText = "阿萨德"
        val encryptedBytes = rsaUtil.encrypt(clearText.toByteArray(StandardCharsets.UTF_8))
        val decryptedText = String(rsaUtil.decrypt(encryptedBytes), StandardCharsets.UTF_8)
        println(decryptedText == clearText)
    }

    /* ======================== other ======================== */
}