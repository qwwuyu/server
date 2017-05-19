package com.qwwuyu.server.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexCtrl {
	@RequestMapping("")
	public String toWelcome(HttpServletRequest request, Model model) {
		return "/WEB-INF/jsp/index.jsp";
	}

	@RequestMapping("/**")
	public String toAll(HttpServletRequest request, Model model) {
		return "/WEB-INF/jsp/index.jsp";
	}
}