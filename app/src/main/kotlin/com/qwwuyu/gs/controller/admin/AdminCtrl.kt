package com.qwwuyu.gs.controller.admin

import com.qwwuyu.gs.configs.Constant
import com.qwwuyu.gs.filter.AuthRequired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
@AuthRequired(permit = Constant.PERMIT_ADMIN, toAdmin = true)
@RequestMapping("/ad")
class AdminCtrl {
    private fun hand(path: String): String {
        return Constant.PREFIX + "admin/" + path
    }

    @AuthRequired(anth = false)
    @RequestMapping("/login")
    fun toLogin(request: HttpServletRequest): String {
        return hand("login.html")
    }

    @RequestMapping("")
    fun toIndex(request: HttpServletRequest): String {
        return hand("ad.html")
    }

    @RequestMapping("file")
    fun toFileManager(request: HttpServletRequest): String {
        return hand("file_manager.html")
    }

    @RequestMapping("/java")
    fun toJava(request: HttpServletRequest): String {
        return hand("java.html")
    }
}