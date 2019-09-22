package com.qwwuyu.server.ctrl;

import com.qwwuyu.server.utils.JavaRunUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

//@Controller
@RequestMapping("/java")
public class JavaCtrl {
    @RequestMapping("")
    public String toJava(HttpServletRequest request, Model model) {
        return "/WEB-INF/jsp/java.jsp";
    }

    @RequestMapping("/result")
    public String toJavaResult(HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        model.addAttribute("result", JavaRunUtil.run(code));
        return "/WEB-INF/jsp/java_result.jsp";
    }
}