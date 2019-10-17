package com.qwwuyu.server.ctrl;

import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.filter.AuthRequired;
import com.qwwuyu.server.utils.J2EEUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AuthRequired(permit = Constant.PERMIT_ADMIN)
@Controller
public class HttpsCtrl {
    private static Map<String, String> map = new HashMap<>();

    @RequestMapping("/https/set")
    public void setKey(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getAttribute(Constant.KEY_USER);
        if (user == null) throw new RuntimeException("user is null");
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        if (J2EEUtil.isNull(response, key, value)) return;
        map.put(key, value);
        J2EEUtil.render(response, J2EEUtil.getSuccessBean());
    }

    @RequestMapping("/https/clear")
    public void clearKey(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getAttribute(Constant.KEY_USER);
        if (user == null) throw new RuntimeException("user is null");
        map.clear();
        J2EEUtil.render(response, J2EEUtil.getSuccessBean());
    }

    @AuthRequired(anth = false)
    @RequestMapping("/.well-known/acme-challenge/*")
    public void ssl1(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        String index = "/acme-challenge/";
        String key = requestURI.substring(requestURI.indexOf(index) + index.length());
        String value = map.get(key);
        response.setContentType("text/plain;charset=utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            if (value != null) {
                response.getWriter().write(value);
            } else {
                response.getWriter().write("no data");
            }
        } catch (IOException ignored) {
        }
    }
}