package com.qwwuyu.gs.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/error")
class ErrorCtrl {
    @RequestMapping("/401")
    fun error401(request: HttpServletRequest, response: HttpServletResponse) {
        error(response, 401)
    }

    @RequestMapping("/404")
    fun error404(request: HttpServletRequest, response: HttpServletResponse) {
        error(response, 404)
    }

    @RequestMapping("/405")
    fun error405(request: HttpServletRequest, response: HttpServletResponse) {
        error(response, 405)
    }

    @RequestMapping("/500")
    fun error500(request: HttpServletRequest, response: HttpServletResponse) {
        error(response, 500)
    }

    private fun error(response: HttpServletResponse, code: Int) {
        response.status = code
        response.contentType = "text/plain;charset=utf-8"
        response.setHeader("Pragma", "No-cache")
        response.setHeader("Cache-Control", "no-cache")
        response.setDateHeader("Expires", 0)
    }
}