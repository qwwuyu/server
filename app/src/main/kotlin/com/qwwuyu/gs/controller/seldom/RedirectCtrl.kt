package com.qwwuyu.gs.controller.seldom

import com.google.gson.Gson
import com.qwwuyu.gs.utils.AppUtil
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class RedirectCtrl {
    companion object {
        private val redirectMap: MutableMap<String, String> = HashMap()
    }

    @RequestMapping(value = ["/to/set"], method = [RequestMethod.GET, RequestMethod.POST])
    fun setRedirect(
        request: HttpServletRequest, response: HttpServletResponse,
        @RequestParam("path") path: String, @RequestParam("location") location: String
    ) {
        redirectMap[path] = location
        ok(response, AppUtil.getSuccessBean())
    }

    @RequestMapping(value = ["/to/*"], method = [RequestMethod.GET])
    fun to(request: HttpServletRequest, response: HttpServletResponse) {
        val requestURI = request.requestURI
        val path = requestURI.substring(requestURI.indexOf("/to") + 4)
        val location = redirectMap[path]
        if (location.isNullOrEmpty()) {
            ok(response, AppUtil.getErrorBean())
        } else {
            response.sendRedirect(location)
        }
    }

    private fun ok(response: HttpServletResponse, any: Any) {
        response.contentType = "application/json;charset=utf-8"
        response.setHeader("Pragma", "No-cache")
        response.setHeader("Cache-Control", "no-cache")
        response.setDateHeader("Expires", 0)
        try {
            response.writer.write(Gson().toJson(any))
        } catch (ignored: IOException) {
        }
    }
}