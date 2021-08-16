package com.qwwuyu.gs.filter

import com.qwwuyu.gs.configs.Constant
import com.qwwuyu.gs.entity.User
import com.qwwuyu.gs.service.UserService
import com.qwwuyu.gs.utils.AppUtil
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthInterceptor : HandlerInterceptor {
    @Resource
    private lateinit var userService: UserService

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        var authRequired = handler.method.getAnnotation(AuthRequired::class.java)
        if (authRequired == null) {
            authRequired = handler.beanType.getAnnotation(AuthRequired::class.java)
        }
        if (authRequired == null || !authRequired.anth) {
            return true
        }
        val toAdmin = authRequired.toAdmin
        val user = checkPermit(
            authRequired.permit,
            authRequired.code,
            authRequired.expire,
            !toAdmin,
            userService,
            request,
            response
        )
        if (user == null) {
            if (toAdmin) {
                response.status = HttpServletResponse.SC_MOVED_TEMPORARILY
                response.setHeader("Location", "/")
            }
            return false
        }
        request.setAttribute(Constant.KEY_USER, user)
        return true
    }

    private fun checkPermit(
        permit: Int, code: Int, expire: Boolean, render: Boolean,
        userService: UserService, request: HttpServletRequest, response: HttpServletResponse
    ): User? {
        val token = AppUtil.getToken(request)
        if (token == null || token.isEmpty()) {
            if (render) AppUtil.renderInfo(response, "请先登录", code)
            return null
        }
        val user = userService.selectByUser(User(token = token))
        if (user == null) {
            if (render) AppUtil.renderInfo(response, "请先登录", code)
            return null
        } else if (user.auth!! < permit) {
            if (render) AppUtil.renderInfo(response, "权限不足", code)
            return null
        } else if (expire && System.currentTimeMillis() - user.time!! > Constant.expiresValue) {
            if (render) AppUtil.renderInfo(response, "验证已过期", code)
            return null
        }
        return user
    }
}