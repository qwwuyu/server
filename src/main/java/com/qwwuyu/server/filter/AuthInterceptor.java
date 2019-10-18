package com.qwwuyu.server.filter;

import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.J2EEUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
    @Resource
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AuthRequired authRequired = handlerMethod.getMethod().getAnnotation(AuthRequired.class);
        if (authRequired == null) {
            authRequired = handlerMethod.getBeanType().getAnnotation(AuthRequired.class);
        }
        if (authRequired == null || !authRequired.anth()) {
            return true;
        }
        boolean toAdmin = authRequired.toAdmin();
        User user = checkPermit(authRequired.permit(), authRequired.code(), authRequired.expire(), !toAdmin,
                this.userService, request, response);
        if (user == null) {
            if (toAdmin) {
                response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                response.setHeader("Location", "/ad/login");
            }
            return false;
        }
        request.setAttribute(Constant.KEY_USER, user);
        return true;
    }

    private static User checkPermit(int permit, int code, boolean expire, boolean render,
                                    IUserService userService, HttpServletRequest request, HttpServletResponse response) {
        String token = J2EEUtil.getToken(request);
        if (token == null || token.length() == 0) {
            if (render) J2EEUtil.renderInfo(response, "请先登录", code);
            return null;
        }
        User user = userService.selectByUser(new User().setToken(token));
        if (user == null) {
            if (render) J2EEUtil.renderInfo(response, "请先登录", code);
            return null;
        } else if (user.getAuth() < permit) {
            if (render) J2EEUtil.renderInfo(response, "权限不足", code);
            return null;
        } else if (expire && System.currentTimeMillis() - user.getTime() > Constant.expiresValue) {
            if (render) J2EEUtil.renderInfo(response, "验证已过期", code);
            return null;
        }
        return user;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
