package com.qwwuyu.gs.controller.seldom

import com.google.gson.Gson
import com.qwwuyu.gs.configs.Constant
import com.qwwuyu.gs.filter.AuthRequired
import com.qwwuyu.gs.service.WebSocketServer
import com.qwwuyu.gs.utils.AppUtil
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/i/ws")
@AuthRequired(permit = Constant.PERMIT_ADMIN, code = HttpServletResponse.SC_UNAUTHORIZED)
class WSCtrl {
    @RequestMapping(value = ["/sendText"], method = [RequestMethod.GET])
    fun sendText(
        request: HttpServletRequest, response: HttpServletResponse,
        @RequestParam("id") id: String, @RequestParam("text") text: String
    ) {
        WebSocketServer.sendMsg(id, text)
        ok(response)
    }

    @RequestMapping(value = ["/sendTopic"], method = [RequestMethod.GET])
    fun sendTopic(
        request: HttpServletRequest, response: HttpServletResponse,
        @RequestParam("topic") topic: String, @RequestParam("text") text: String
    ) {
        WebSocketServer.sendTopic(topic, text)
        ok(response)
    }

    private fun ok(response: HttpServletResponse) {
        response.contentType = "application/json;charset=utf-8"
        response.setHeader("Pragma", "No-cache")
        response.setHeader("Cache-Control", "no-cache")
        response.setDateHeader("Expires", 0)
        try {
            response.writer.write(Gson().toJson(AppUtil.getSuccessBean()))
        } catch (ignored: IOException) {
        }
    }
}