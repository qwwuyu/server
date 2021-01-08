package com.qwwuyu.gs.controller

import com.qwwuyu.gs.configs.Constant
import com.qwwuyu.gs.utils.AppUtil
import com.qwwuyu.lib.utils.CommUtil
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
class HtmlCtrl {
    private fun hand(path: String): String {
        return Constant.PREFIX + "static/" + path
    }

    @RequestMapping("/")
    fun toWelcome(request: HttpServletRequest): String {
        return when {
            "/" != request.requestURI -> hand("404.html")
            CommUtil.isExist(AppUtil.getToken(request)) -> hand("index.html")
            else -> hand("empty.html")
        }
    }

    @RequestMapping(value = ["/card", "/note", "/flag"])
    fun toWelcome2(request: HttpServletRequest): String {
        return hand("index.html")
    }

    @RequestMapping(value = ["/card/send", "/note/send", "/flag/send"])
    fun toSend(request: HttpServletRequest): String {
        val requestURI = request.requestURI
        val path = requestURI.substring(requestURI.indexOf("/") + 1, requestURI.indexOf("/send"))
        return hand("send_${path}.html")
    }

    @RequestMapping(value = ["/note/content"])
    fun toContent(request: HttpServletRequest): String {
        val requestURI = request.requestURI
        val path = requestURI.substring(requestURI.indexOf("/") + 1, requestURI.indexOf("/content"))
        return hand("content_${path}.html")
    }

    @RequestMapping("/robots.txt")
    fun robots(request: HttpServletRequest?): String {
        return hand("robots.txt")
    }

    @RequestMapping("/error500")
    fun to500(request: HttpServletRequest?): String {
        return hand("500.html")
    }

    @RequestMapping("/error404")
    fun to404(request: HttpServletRequest?): String {
        return hand("404.html")
    }

    @RequestMapping("/test")
    fun test(request: HttpServletRequest?): String {
        return Constant.PREFIX + "test/test.html"
    }
}