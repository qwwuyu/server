package com.qwwuyu.gs.filter

import com.qwwuyu.gs.utils.AppUtil
import com.qwwuyu.lib.utils.J2EEUtil
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class IpInterceptor : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger("ip")
    private val map: MutableMap<String, Array<Long>> = HashMap()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestURI = request.requestURI
        val logAddress = J2EEUtil.getLogAddress(request)
        val address = J2EEUtil.getRealAddress(request)
        val method = request.method
        logger.info("requestURI:$requestURI method:$method address:$address >> $logAddress")
        return when (requestURI) {
            "/i/auth/register" -> limit(address + requestURI, response, 10L, 86400000L)
            "/i/auth/login" -> limit(address + requestURI, response, 5L, 60000L)
            "/i/blog/card/send" -> limit(address + requestURI, response, 5L, 60000L)
            else -> true
        }
    }

    private fun limit(key: String, response: ServletResponse, maxNum: Long, maxTime: Long): Boolean {
        val value = map[key]
        if (value != null) {
            val num = value[0] + 1
            val time = value[1]
            when {
                // 时间超出限制
                (System.currentTimeMillis() - time > maxTime) -> map[key] = arrayOf(1L, System.currentTimeMillis())
                (num > maxNum) -> {
                    AppUtil.render(response as HttpServletResponse, AppUtil.getErrorBean().info("请求次数限制"))
                    return false
                }
                else -> map[key] = arrayOf(num, time)
            }
        } else {
            map[key] = arrayOf(1L, System.currentTimeMillis())
        }
        return true
    }
}