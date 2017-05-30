package com.qwwuyu.server.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.bean.Flag;
import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.service.IFlagService;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.ResponseUtil;

@Controller
@RequestMapping("/i/flag")
public class FlagCtrl {
	@Resource
	private IUserService userService;
	@Resource
	private IFlagService service;

	@RequestMapping("/get")
	public void getFlag(HttpServletRequest request, HttpServletResponse response) {
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
			page = page > 0 ? page : 1;
		} catch (Exception e) {
		}
		ResponseUtil.render(response, ResponseBean.getSuccessBean().setData(service.getFlag(page)));
	}

	@RequestMapping("/send")
	public void sendFlag(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("auth");
		String title = request.getParameter("title");
		if (J2EEUtil.isNull(response, token)) return;
		User user = userService.selectByUser(new User().setToken(token));
		if (user == null) {
			J2EEUtil.renderInfo(response, "请先登录");
			return;
		}
		if (user.getAuth() != 5) {
			J2EEUtil.renderInfo(response, "请先登录");
			return;
		}
		if (title == null || !title.matches(".{1,50}") || !title.matches(".*[\\S]+.*")) {
			J2EEUtil.renderInfo(response, "内容不能为空");
			return;
		}
		Flag flag = new Flag().setNick(user.getNick()).setTime(System.currentTimeMillis()).setTitle(title)
				.setUserId(user.getId());
		service.insert(flag);
		ResponseUtil.render(response, ResponseBean.getSuccessBean());
	}
}