package com.qwwuyu.server.ctrl.admin;

import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.filter.AuthRequired;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.JavaRunUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/java")
@AuthRequired(permit = Constant.PERMIT_ADMIN)
public class JavaCtrl {
    @RequestMapping("/result")
    public void toJavaResult(HttpServletRequest request, HttpServletResponse response) {
        User user = J2EEUtil.getUser(request);

        String code = request.getParameter("code");
        code = code == null ? "" : code;
        String html = "<!doctype html><html><body><pre>" + JavaRunUtil.run(code) + "</pre></body></html>";

        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            response.getWriter().write(html);
        } catch (IOException ignored) {
        }
    }
}