package com.qwwuyu.server.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.bean.Test;
import com.qwwuyu.server.service.ITestService;
import com.qwwuyu.server.utils.JavaRunUtil;

@Controller
public class TestCtrl {
	@Resource
	private ITestService service;

	@RequestMapping("")
	public String toWelcome(HttpServletRequest request, Model model) {
		Test test = this.service.getTestById(1);
		model.addAttribute("test", test.getContent());
		return "/WEB-INF/jsp/show.jsp";
	}

	@RequestMapping("/java")
	public String toJava(HttpServletRequest request, Model model) {
		return "/WEB-INF/jsp/java.jsp";
	}

	@RequestMapping("/java/result")
	public String toJavaResult(HttpServletRequest request, Model model) {
		String code = request.getParameter("code");
		model.addAttribute("result", JavaRunUtil.run(code));
		return "/WEB-INF/jsp/java_result.jsp";
	}

	@RequestMapping("/**")
	public String toAll(HttpServletRequest request, Model model) {
		String text = request.getParameter("text");
		Test test = this.service.getTestById(1);
		if (text != null) {
			test.setContent(text);
			this.service.setTest(test);
		}
		model.addAttribute("test", test.getContent());
		return "/WEB-INF/jsp/show.jsp";
	}
}