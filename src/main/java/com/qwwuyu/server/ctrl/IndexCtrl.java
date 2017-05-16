package com.qwwuyu.server.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.bean.Test;
import com.qwwuyu.server.service.ITestService;

@Controller
public class IndexCtrl {
	@Resource
	private ITestService service;

	@RequestMapping("")
	public String toWelcome(HttpServletRequest request, Model model) {
		model.addAttribute("test", "index");
		return "/WEB-INF/jsp/index.jsp";
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
		return "/WEB-INF/jsp/index.jsp";
	}
}