package com.qwwuyu.server.ctrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.ResponseUtil;

@Controller
public class HttpsCtrl {
	private static Map<String, String> map = new HashMap<>();
	@Resource private IUserService userService;

	@RequestMapping("/https/set")
	public void getNote(HttpServletRequest request, HttpServletResponse response) {
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		if (J2EEUtil.isNull(response, key, value)) return;
		if (null == J2EEUtil.checkPermit(5, userService, request, response)) return;
		map.put(key, value);
		ResponseUtil.render(response, ResponseBean.getSuccessBean());
	}

	@RequestMapping("/https/clear")
	public void sendCard(HttpServletRequest request, HttpServletResponse response) {
		if (null == J2EEUtil.checkPermit(5, userService, request, response)) return;
		map.clear();
		ResponseUtil.render(response, ResponseBean.getSuccessBean());
	}

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
		} catch (IOException e) {}
	}
}