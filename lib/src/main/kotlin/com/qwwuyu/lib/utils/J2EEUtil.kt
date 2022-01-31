package com.qwwuyu.lib.utils

import javax.servlet.http.HttpServletRequest

object J2EEUtil {
    /** 获取真实ip  */
    fun getRealAddress(request: HttpServletRequest): String {
        val remoteAddr = request.remoteAddr
        if ("127.0.0.1" != remoteAddr && "0:0:0:0:0:0:0:1" != remoteAddr) {
            return remoteAddr
        }
        val realIp = request.getHeader("X-Real-IP")
        var ip = realIp
        if (!ip.isNullOrEmpty() && !ip.equals("unknown", ignoreCase = true) && !ip.equals("0:0:0:0:0:0:0:1")
            && !ip.startsWith("10.") && !ip.startsWith("192.168") && !ip.startsWith("172.")
        ) {
            return ip
        }
        ip = request.getHeader("x-forwarded-for")
        if (ip.contains(",")) {
            val ips = ip.split(",")
            ip = ips[ips.size - 2]
        }
        if (!ip.isNullOrEmpty() && !ip.equals("unknown", ignoreCase = true) && !ip.equals("0:0:0:0:0:0:0:1")
            && !ip.startsWith("10.") && !ip.startsWith("192.168") && !ip.startsWith("172.")
        ) {
            return ip
        }
        return realIp
    }

    /** 获取日志ip  */
    fun getLogAddress(request: HttpServletRequest): String {
        var log = " remoteAddr=${request.remoteAddr}"
        val headers = listOf("X-Real-IP", "x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP")
        headers.forEach {
            val ip = request.getHeader(it)
            if (!ip.isNullOrEmpty() && !ip.equals("unknown", ignoreCase = true)) {
                log += " ${it}=${ip}"
            }
        }
        return log
    }
}