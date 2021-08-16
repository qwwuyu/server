package com.qwwuyu.gs.controller

import com.qwwuyu.gs.configs.Constant
import com.qwwuyu.gs.entity.ResponseBean
import com.qwwuyu.gs.entity.User
import com.qwwuyu.gs.service.UserService
import com.qwwuyu.gs.utils.AppUtil
import com.qwwuyu.lib.utils.J2EEUtil
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/i/auth")
class HomeAuthCtrl {
    @Resource
    private lateinit var userService: UserService

    @RequestMapping("/login")
    fun login(request: HttpServletRequest, response: HttpServletResponse) {
        val acc = request.getParameter("acc")
        val pwd = request.getParameter("pwd")
        if (AppUtil.isNull(response, acc, pwd)) return
        if (AppUtil.renderInfo(response, check(acc, null, null))) return
        login(response, acc, pwd)
    }

    @RequestMapping("/register")
    fun register(request: HttpServletRequest, response: HttpServletResponse) {
        val acc = request.getParameter("acc")
        val nick = request.getParameter("nick").trim { it <= ' ' }.replace("\\s{2,}".toRegex(), " ")
        var pwd = request.getParameter("pwd")
        if (AppUtil.isNull(response, acc, nick, pwd)) return
        pwd = try {
            AppUtil.defaultRSA().decrypt(pwd)
        } catch (e: Exception) {
            AppUtil.renderInfo(response, "服务器解密数据失败")
            return
        }
        if (AppUtil.renderInfo(response, check(acc, nick, pwd))) return
        if (userService.selectByUser(User(name = acc)) != null) {
            AppUtil.render(response, ResponseBean(Constant.HTTP_ACC_EXIST, "帐号已存在"))
            return
        }
        if (userService.selectByUser(User(nick = nick)) != null) {
            AppUtil.render(response, ResponseBean(Constant.HTTP_NIKE_EXIST, "昵称已存在"))
            return
        }
        val user = User(null, acc, AppUtil.handPwd(acc, pwd), nick, 2, J2EEUtil.getAddress(request), null, null, 0L, 0L)
        userService.insert(user)
        login(response, acc, user.pwd!!)
    }

    @RequestMapping("/checkToken")
    fun checkToken(request: HttpServletRequest, response: HttpServletResponse) {
        val token = request.getParameter("token")
        if (AppUtil.isNull(response, token)) return
        val user = userService.selectByUser(User(token = token))
        if (null == user) {
            AppUtil.render(response, ResponseBean(Constant.HTTP_AUTH_OTHER, "帐号在其他地方登录"))
            return
        } else if (System.currentTimeMillis() - user.time!! > Constant.expiresValue) {
            AppUtil.render(response, ResponseBean(Constant.HTTP_AUTH_EXPIRE, "验证已过期"))
            return
        }
        user.time = System.currentTimeMillis()
        userService.updateByPrimaryKeySelective(user)
        AppUtil.render(response, AppUtil.getSuccessBean())
    }

    private fun login(response: HttpServletResponse, acc: String, pwd: String) {
        val user = userService.selectByUser(User(name = acc, pwd = pwd))
        if (null == user) {
            AppUtil.renderInfo(response, "帐号不存在或密码错误")
            return
        }
        user.token = AppUtil.makeToken(user)
        user.time = System.currentTimeMillis()
        userService.updateByPrimaryKeySelective(user)
        AppUtil.render(response, AppUtil.getSuccessBean().data(user.token))
    }

    private fun check(acc: String, nick: String?, pwd: String?): String? {
        return if (!acc.matches("[\\w]{4,20}".toRegex())) {
            "帐号不低于4位的单词字符"
        } else if (nick != null && (nick.length < 2 || nick.length > 20)) {
            "昵称不低于2位"
        } else if (pwd != null && pwd.length < 6) {
            "密码不低于6位"
        } else {
            null
        }
    }
}