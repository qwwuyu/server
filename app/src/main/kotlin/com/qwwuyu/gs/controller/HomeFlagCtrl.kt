package com.qwwuyu.gs.controller

import com.qwwuyu.gs.configs.Constant
import com.qwwuyu.gs.entity.Flag
import com.qwwuyu.gs.filter.AuthRequired
import com.qwwuyu.gs.service.FlagService
import com.qwwuyu.gs.utils.AppUtil
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/i/flag")
class HomeFlagCtrl {
    @Resource
    private lateinit var service: FlagService

    @RequestMapping("/get")
    fun getFlag(request: HttpServletRequest, response: HttpServletResponse) {
        var page = 1
        try {
            page = request.getParameter("page").toInt()
            page = if (page > 0) page else 1
        } catch (ignored: Exception) {
        }
        AppUtil.render(response, AppUtil.getSuccessBean().data(service.getFlag(page)))
    }

    @AuthRequired(permit = Constant.PERMIT_ADMIN)
    @RequestMapping("/send")
    fun sendFlag(request: HttpServletRequest, response: HttpServletResponse) {
        val user = AppUtil.getUser(request)
        val title = request.getParameter("title")
        if (title == null || !title.matches(".{1,50}".toRegex()) || !title.matches(".*[\\S]+.*".toRegex())) {
            AppUtil.renderInfo(response, "内容不能为空")
            return
        }
        val flag = Flag(userId = user.id, nick = user.nick, title = title, time = System.currentTimeMillis())
        service.insert(flag)
        AppUtil.render(response, AppUtil.getSuccessBean())
    }
}