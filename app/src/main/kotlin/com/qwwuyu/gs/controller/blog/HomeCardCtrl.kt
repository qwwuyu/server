package com.qwwuyu.gs.controller.blog

import com.qwwuyu.gs.configs.Constant
import com.qwwuyu.gs.entity.Card
import com.qwwuyu.gs.filter.AuthRequired
import com.qwwuyu.gs.service.CardService
import com.qwwuyu.gs.utils.AppUtil
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/i/blog/card")
class HomeCardCtrl {
    @Resource
    private lateinit var service: CardService

    @RequestMapping("/get")
    fun getCard(request: HttpServletRequest, response: HttpServletResponse) {
        var page = 1
        try {
            page = request.getParameter("page").toInt()
            page = if (page > 0) page else 1
        } catch (ignored: Exception) {
        }
        AppUtil.render(response, AppUtil.getSuccessBean().data(service.getCard(page)))
    }

    @AuthRequired
    @RequestMapping("/send")
    fun sendCard(request: HttpServletRequest, response: HttpServletResponse) {
        val user = AppUtil.getUser(request)
        val title = request.getParameter("title")
        if (title == null || !title.matches(".{1,50}".toRegex()) || !title.matches(".*[\\S]+.*".toRegex())) {
            AppUtil.renderInfo(response, "内容不能为空")
            return
        }
        val card = Card(userId = user.id, nick = user.nick, title = title, time = System.currentTimeMillis())
        service.insert(card)
        AppUtil.render(response, AppUtil.getSuccessBean())
    }

    @AuthRequired
    @RequestMapping("/rm")
    fun rmCard(request: HttpServletRequest, response: HttpServletResponse) {
        val user = AppUtil.getUser(request)
        val cardId: Int = try {
            request.getParameter("id").toInt()
        } catch (e: Exception) {
            AppUtil.renderInfo(response, "参数不正确")
            return
        }
        val card = service.selectByPrimaryKey(cardId)
        if (card == null) {
            AppUtil.renderInfo(response, "card不存在")
            return
        }
        if (Constant.PERMIT_ADMIN != user.auth && card.userId != user.id) {
            AppUtil.renderInfo(response, "你没有这个权限")
            return
        }
        service.deleteByPrimaryKey(cardId)
        AppUtil.render(response, AppUtil.getSuccessBean())
    }
}