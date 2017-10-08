package com.qwwuyu.server.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.CommUtil;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.ResponseUtil;
import com.scienjus.smartqq.Robot;

@Controller
@RequestMapping("/robot")
public class RobotCtrl {
	@Resource private IUserService userService;

	@RequestMapping("")
	public String toRobot(HttpServletRequest request, Model model) {// 165 x 165
		return "/WEB-INF/jsp/robot.jsp";
	}

	@RequestMapping("/open")
	public void openQQ(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("auth");
		String tag = request.getParameter("tag");
		String pwd = request.getParameter("pwd");
		if (J2EEUtil.isNull(response, token, tag, pwd)) return;
		User user = userService.selectByUser(new User().setToken(token));
		if (user == null) {
			J2EEUtil.renderInfo(response, "请先登录");
			return;
		}
		if (user.getAuth() < 3) {
			J2EEUtil.renderInfo(response, "权限不足");
			return;
		}
		new Thread(() -> Robot.addSmartQQ(tag, pwd)).start();
		CommUtil.sleep(2000);
		ResponseUtil.render(response, ResponseBean.getSuccessBean());
	}

	@RequestMapping("/close")
	public void closeQQ(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("auth");
		String tag = request.getParameter("tag");
		if (J2EEUtil.isNull(response, token, tag)) return;
		User user = userService.selectByUser(new User().setToken(token));
		if (user == null) {
			J2EEUtil.renderInfo(response, "请先登录");
			return;
		}
		if (user.getAuth() < 3) {
			J2EEUtil.renderInfo(response, "权限不足");
			return;
		}
		new Thread(() -> Robot.closeSmartQQ(tag)).start();
		ResponseUtil.render(response, ResponseBean.getSuccessBean());
	}
}