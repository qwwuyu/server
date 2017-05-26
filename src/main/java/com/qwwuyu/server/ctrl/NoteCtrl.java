package com.qwwuyu.server.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.service.INoteService;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.J2EEUtil;

@Controller
@RequestMapping("/i")
public class NoteCtrl {
	@Resource
	private IUserService userService;
	@Resource
	private INoteService noteService;

	@RequestMapping("/{card|note|flag|tool}")
	public void flag(HttpServletRequest request, HttpServletResponse response) {
		String auth = request.getParameter("token");
		String pageStr = request.getParameter("page");
		User user;
		if (auth != null) {
			user = userService.selectByUser(new User().setToken(auth));
		}
		int page = 1;
		try {
			page = Integer.parseInt(pageStr);
		} catch (Exception e) {
		}
		J2EEUtil.renderInfo(response, "结束");
	}
}