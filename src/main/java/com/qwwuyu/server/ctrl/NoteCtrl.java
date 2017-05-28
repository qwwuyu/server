package com.qwwuyu.server.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.service.INoteService;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.ResponseUtil;

@Controller
@RequestMapping("/i/note")
public class NoteCtrl {
	@Resource
	private IUserService userService;
	@Resource
	private INoteService service;

	@RequestMapping("/get")
	public void getNote(HttpServletRequest request, HttpServletResponse response) {
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (Exception e) {
		}
		String data = service.getNote(page);
		ResponseUtil.render(response, ResponseBean.getSuccessBean().setData(data));
	}

	@RequestMapping("/rm")
	public void rmNote(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("auth");
		String card_id = request.getParameter("id");
		J2EEUtil.isNull(response, token, card_id);
		User user = userService.selectByUser(new User().setToken(token));
	}
}