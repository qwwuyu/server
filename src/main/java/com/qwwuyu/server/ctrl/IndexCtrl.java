package com.qwwuyu.server.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexCtrl {
	@RequestMapping("/")
	public String toWelcome(HttpServletRequest request, Model model) {
		if ("/".equals(request.getRequestURI())) {
			return "/WEB-INF/jsp/index.jsp";
		} else {
			return "/WEB-INF/jsp/404.jsp";
		}
	}

	@RequestMapping("/{card|note|flag|tool}")
	public String toWelcome2(HttpServletRequest request, Model model) {
		return "/WEB-INF/jsp/index.jsp";
	}

	@RequestMapping("/error500")
	public String to500(HttpServletRequest request, Model model) {
		return "/WEB-INF/jsp/500.jsp";
	}

	@RequestMapping("/error404")
	public String to404(HttpServletRequest request, Model model) {
		return "/WEB-INF/jsp/404.jsp";
	}
}