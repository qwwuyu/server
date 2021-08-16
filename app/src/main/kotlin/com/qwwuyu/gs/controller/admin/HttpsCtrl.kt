package com.qwwuyu.gs.controller.admin

import com.qwwuyu.gs.configs.Constant
import com.qwwuyu.gs.filter.AuthRequired
import com.qwwuyu.gs.utils.AppUtil
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@AuthRequired(permit = Constant.PERMIT_ADMIN)
@Controller
class HttpsCtrl {
    companion object {
        private val map: MutableMap<String, String> = HashMap()
    }

    @RequestMapping("/i/https/set")
    fun setKey(request: HttpServletRequest, response: HttpServletResponse) {
        val key = request.getParameter("key")
        val value = request.getParameter("value")
        if (AppUtil.isNull(response, key, value)) return
        map[key] = value
        AppUtil.render(response, AppUtil.getSuccessBean())
    }

    @RequestMapping("/i/https/clear")
    fun clearKey(request: HttpServletRequest?, response: HttpServletResponse) {
        map.clear()
        AppUtil.render(response, AppUtil.getSuccessBean())
    }

    @AuthRequired(anth = false)
    @RequestMapping("/.well-known/acme-challenge/*")
    fun ssl1(request: HttpServletRequest, response: HttpServletResponse) {
        val requestURI = request.requestURI
        val index = "/acme-challenge/"
        val key = requestURI.substring(requestURI.indexOf(index) + index.length)
        val value = map[key]
        response.contentType = "text/plain;charset=utf-8"
        response.setHeader("Pragma", "No-cache")
        response.setHeader("Cache-Control", "no-cache")
        response.setDateHeader("Expires", 0)
        if (value != null) {
            response.writer.write(value)
        } else {
            response.writer.write("no data")
        }
    }
}