package com.qwwuyu.server.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.service.ICardService;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.ResponseUtil;

@Controller
@RequestMapping("/i/card")
public class CardCtrl {
	@Resource
	private IUserService userService;
	@Resource
	private ICardService service;

	@RequestMapping("/get")
	public void getCard(HttpServletRequest request, HttpServletResponse response) {
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
			page = page > 0 ? page : 1;
		} catch (Exception e) {
		}
		ResponseUtil.render(response, ResponseBean.getSuccessBean().setData(service.getCard(page)));
	}

	@RequestMapping("/rm")
	public void rmCard(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("auth");
		String card_id = request.getParameter("id");
		J2EEUtil.isNull(response, token, card_id);
		User user = userService.selectByUser(new User().setToken(token));
	}
}