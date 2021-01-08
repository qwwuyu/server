package com.qwwuyu.gs.controller.admin

import com.qwwuyu.gs.configs.Constant
import com.qwwuyu.gs.filter.AuthRequired
import com.qwwuyu.gs.utils.AppUtil
import com.qwwuyu.lib.utils.JavaRunUtil
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/java")
@AuthRequired(permit = Constant.PERMIT_ADMIN)
class JavaCtrl {
    @RequestMapping("/result")
    fun toJavaResult(request: HttpServletRequest, response: HttpServletResponse) {
        val code = request.getParameter("code") ?: ""
        val file = AppUtil.nextJavaFile()
        val html = "<!doctype html><html><body><pre>" + JavaRunUtil.run(code, file) + "</pre></body></html>"
        response.contentType = "text/html;charset=utf-8"
        response.setHeader("Pragma", "No-cache")
        response.setHeader("Cache-Control", "no-cache")
        response.setDateHeader("Expires", 0)
        response.writer.write(html)
    }
}