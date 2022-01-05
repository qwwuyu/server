package com.qwwuyu.lib.utils

import javax.servlet.http.HttpServletRequest

object J2EEUtil {
    /** 获取真实ip  */
    fun getRealAddress(request: HttpServletRequest): String {
        val headers = listOf(
            "x-forwarded-for", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
        )
        headers.forEach {
            val ip = request.getHeader(it)
            if (!ip.isNullOrEmpty() && !ip.equals("unknown", ignoreCase = true)
                && !ip.startsWith("10.") && !ip.startsWith("192.168") && !ip.startsWith("172.")
            ) {
                return ip
            }
        }
        return request.remoteAddr
    }

    /** 获取日志ip  */
    fun getLogAddress(request: HttpServletRequest): String {
        var log = " remoteAddr=${request.remoteAddr}"
        val headers = listOf("x-forwarded-for", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP")
        headers.forEach {
            val ip = request.getHeader(it)
            if (!ip.isNullOrEmpty() && !ip.equals("unknown", ignoreCase = true)) {
                log += " ${it}=${ip}"
            }
        }
        return log
    }
}