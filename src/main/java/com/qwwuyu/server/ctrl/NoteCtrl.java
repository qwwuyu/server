package com.qwwuyu.server.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.service.INoteService;
import com.qwwuyu.server.utils.J2EEUtil;

@Controller
@RequestMapping("/note")
public class NoteCtrl {
	@Resource
	private INoteService service;

	@RequestMapping("/flag")
	public void login(HttpServletRequest request, HttpServletResponse response) {
		String acc = request.getParameter("acc");
		String pwd = request.getParameter("pwd");
		if (J2EEUtil.isNull(response, acc, pwd)) return;
	}
}