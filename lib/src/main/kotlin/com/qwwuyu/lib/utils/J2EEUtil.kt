package com.qwwuyu.lib.utils

import javax.servlet.http.HttpServletRequest

object J2EEUtil {
    /** 获取客户ip  */
    fun getRealAddress(request: HttpServletRequest): String? {
        var ip = request.getHeader("x-forwarded-for")
        if (ip.isNullOrEmpty() || ip.equals("unknown", ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || ip.equals("unknown", ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || ip.equals("unknown", ignoreCase = true)) {
            ip = request.remoteAddr
        }
        return ip
    }

    /** 获取访问ip  */
    fun getAddress(request: HttpServletRequest): String {
        return request.remoteAddr
    }
}