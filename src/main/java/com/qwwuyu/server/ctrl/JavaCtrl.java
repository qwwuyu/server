package com.qwwuyu.server.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.utils.JavaRunUtil;

@Controller
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