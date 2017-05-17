package com.qwwuyu.server.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.RSAUtil;
import com.qwwuyu.server.utils.ResponseUtil;

@Controller
@RequestMapping("/i")
public class AuthServlet {
	@Resource
	private IUserService service;

	@RequestMapping("/login")
	public void login(HttpServletRequest request, HttpServletResponse response) {
		String acc = request.getParameter("acc");
		String pwd = request.getParameter("pwd");
		if (J2EEUtil.isNull(response, acc, pwd)) {
			return;
		}
		if (J2EEUtil.renderInfo(response, check(acc, null, null))) {
			return;
		}
		login(response, acc, pwd);
	}

	@RequestMapping("/register")
	public void register(HttpServletRequest request, HttpServletResponse response) {
		String acc = request.getParameter("acc");
		String nick = request.getParameter("nick").trim().replaceAll("\\s{2,}", " ");
		String pwd = request.getParameter("pwd");
		if (J2EEUtil.isNull(response, acc, nick, pwd)) {
			return;
		}
		try {
			pwd = RSAUtil.getDefault().decrypt(pwd);
		} catch (Exception e) {
			J2EEUtil.renderInfo(response, "服务器解密数据失败");
			return;
		}
		if (J2EEUtil.renderInfo(response, check(acc, nick, pwd))) {
			return;
		}
		if (service.selectByName(acc) != null) {
			ResponseUtil.render(response, ResponseBean.getErrorBean().setInfo("帐号已存在").setStatu(2));
			return;
		}
		if (service.selectByNick(nick) != null) {
			ResponseUtil.render(response, ResponseBean.getErrorBean().setInfo("昵称已存在").setStatu(2));
			return;
		}
		User user = new User(null, acc, J2EEUtil.handPwd(acc, pwd), nick, "", 2);
		service.insert(user);
		login(response, acc, user.getPwd());
	}

	private void login(HttpServletResponse response, String acc, String pwd) {
		User user = service.selectByName(acc);
		if (null == user || !pwd.equals(user.getPwd())) {
			J2EEUtil.renderInfo(response, "帐号不存在或密码错误");
			return;
		}
		user.setToken(J2EEUtil.getToken(user));
		service.updateByPrimaryKeySelective(user);
		ResponseUtil.render(response, ResponseBean.getSuccessBean().setData(user.getToken()));
	}

	private String check(String acc, String nick, String pwd) {
		if (!acc.matches("[\\w]{4,20}")) {
			return "帐号不低于4位的单词字符";
		} else if (nick != null && (nick.length() < 2 || nick.length() > 20)) {
			return "昵称不低于2位";
		} else if (pwd != null && (pwd.length() < 6)) {
			return "密码不低于6位";
		} else {
			return null;
		}
	}
}