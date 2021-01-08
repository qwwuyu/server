package com.qwwuyu.gs.controller.seldom

import com.google.gson.Gson
import com.qwwuyu.gs.entity.ResponseBean
import com.qwwuyu.lib.utils.CommUtil
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/test")
class TestCtrl {
    @RequestMapping(value = ["/get"], method = [RequestMethod.GET])
    operator fun get(request: HttpServletRequest, response: HttpServletResponse) {
        post(request, response)
    }

    @RequestMapping(value = ["/post"], method = [RequestMethod.POST])
    fun post(request: HttpServletRequest, response: HttpServletResponse) {
        val map = request.parameterMap
        response.contentType = "application/json;charset=utf-8"
        response.setHeader("Pragma", "No-cache")
        response.setHeader("Cache-Control", "no-cache")
        response.setDateHeader("Expires", 0)
        try {
            response.writer.write(Gson().toJson(ResponseBean(1, "", map)))
        } catch (ignored: IOException) {
        }
    }

    @RequestMapping(value = ["/timeout"], method = [RequestMethod.POST, RequestMethod.GET])
    fun timeout(request: HttpServletRequest, response: HttpServletResponse) {
        CommUtil.sleep(100000)
        post(request, response)
    }

    @RequestMapping(value = ["/error"], method = [RequestMethod.POST, RequestMethod.GET])
    fun error(request: HttpServletRequest?, response: HttpServletResponse?) {
        throw RuntimeException("err")
    }
}