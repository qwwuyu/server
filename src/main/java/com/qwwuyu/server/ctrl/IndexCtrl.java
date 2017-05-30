package com.qwwuyu.server.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexCtrl {
	@RequestMapping("/")
	public String toWelcome(HttpServletRequest request) {
		if ("/".equals(request.getRequestURI())) {
			return "/WEB-INF/jsp/index.jsp";
		} else {
			return "/WEB-INF/jsp/404.jsp";
		}
	}

	@RequestMapping({ "/card", "/note", "/flag", })
	public String toWelcome2(HttpServletRequest request) {
		return "/WEB-INF/jsp/index.jsp";
	}

	@RequestMapping({ "/card/send", "/note/send", "/flag/send", })
	public String toSend(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String path = requestURI.substring(requestURI.indexOf("/") + 1, requestURI.indexOf("/send"));
		return "/WEB-INF/jsp/send_" + path + ".jsp";
	}

	@RequestMapping({ "/note/content", })
	public String toContent(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String path = requestURI.substring(requestURI.indexOf("/") + 1, requestURI.indexOf("/content"));
		return "/WEB-INF/jsp/content_" + path + ".jsp";
	}

	@RequestMapping("/error500")
	public String to500(HttpServletRequest request) {
		return "/WEB-INF/jsp/500.jsp";
	}

	@RequestMapping("/error404")
	public String to404(HttpServletRequest request) {
		return "/WEB-INF/jsp/404.jsp";
	}
}