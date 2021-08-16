package com.qwwuyu.gs.controller.blog

import com.qwwuyu.gs.configs.Constant
import com.qwwuyu.gs.entity.Note
import com.qwwuyu.gs.filter.AuthRequired
import com.qwwuyu.gs.service.NoteService
import com.qwwuyu.gs.utils.AppUtil
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/i/blog/note")
class HomeNoteCtrl {
    @Resource
    private lateinit var service: NoteService

    @RequestMapping("/get")
    fun getNote(request: HttpServletRequest, response: HttpServletResponse) {
        var page = 1
        try {
            page = request.getParameter("page").toInt()
            page = if (page > 0) page else 1
        } catch (ignored: Exception) {
        }
        AppUtil.render(response, AppUtil.getSuccessBean().data(service.getNote(page)))
    }

    @AuthRequired(permit = Constant.PERMIT_ADMIN)
    @RequestMapping("/send")
    fun sendCard(request: HttpServletRequest, response: HttpServletResponse) {
        val user = AppUtil.getUser(request)
        val title = request.getParameter("title")
        val content = request.getParameter("content")
        if (AppUtil.isNull(response, content)) return
        if (title == null || !title.matches(".{1,50}".toRegex()) || !title.matches(".*[\\S]+.*".toRegex())) {
            AppUtil.renderInfo(response, "内容不能为空")
            return
        }
        val note = Note(userId = user.id, nick = user.nick, title = title, content = content, time = System.currentTimeMillis())
        service.insert(note)
        AppUtil.render(response, AppUtil.getSuccessBean())
    }

    @AuthRequired(permit = Constant.PERMIT_ADMIN)
    @RequestMapping("/rm")
    fun rmCard(request: HttpServletRequest, response: HttpServletResponse) {
        val noteId: Int = try {
            request.getParameter("id").toInt()
        } catch (e: Exception) {
            AppUtil.renderInfo(response, "参数不正确")
            return
        }
        service.deleteByPrimaryKey(noteId)
        AppUtil.render(response, AppUtil.getSuccessBean())
    }

    @RequestMapping("/content")
    fun contentCard(request: HttpServletRequest, response: HttpServletResponse) {
        val noteId: Int = try {
            request.getParameter("id").toInt()
        } catch (e: Exception) {
            AppUtil.renderInfo(response, "参数不正确")
            return
        }
        val note = service.selectByPrimaryKey(noteId)
        if (note == null) {
            AppUtil.renderInfo(response, "note不存在")
            return
        }
        val map: MutableMap<String, Any> = HashMap()
        map["note"] = note
        map["sysTime"] = System.currentTimeMillis()
        AppUtil.render(response, AppUtil.getSuccessBean().data(map))
    }
}