package com.qwwuyu.server.ctrl;

import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.filter.AuthRequired;
import com.qwwuyu.server.utils.JavaRunUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/java")
@AuthRequired(permit = 5)
public class JavaCtrl {
    @RequestMapping("")
    public String toJava(HttpServletRequest request, Model model) {
        User user = (User) request.getAttribute(Constant.KEY_USER);
        if (user == null) throw new RuntimeException("user is null");
        return "/WEB-INF/jsp/java.jsp";
    }

    @RequestMapping("/result")
    public String toJavaResult(HttpServletRequest request, Model model) {
        User user = (User) request.getAttribute(Constant.KEY_USER);
        if (user == null) throw new RuntimeException("user is null");
        String code = request.getParameter("code");
        code = code == null ? "" : code;
        model.addAttribute("result", JavaRunUtil.run(code));
        return "/WEB-INF/jsp/java_result.jsp";
    }
}