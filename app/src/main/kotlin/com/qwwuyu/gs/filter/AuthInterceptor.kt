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
        val user = AppUtil.checkPermit(
            permit = authRequired.permit,
            code = authRequired.code,
            expire = authRequired.expire,
            render = !toAdmin,
            userService = userService,
            request = request,
            response = response
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
}