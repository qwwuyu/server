package com.qwwuyu.server.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.robot.RobotHelper;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.ResponseUtil;

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
		if (null == J2EEUtil.checkPermit(5, userService, request, response)) return;
		try {
			RobotHelper.getInstance().openRobot();
			ResponseUtil.render(response, ResponseBean.getSuccessBean());
		} catch (Exception e) {
			ResponseUtil.render(response, ResponseBean.getErrorBean().setInfo(e.getMessage()));
		}
	}

	@RequestMapping("/close")
	public void closeQQ(HttpServletRequest request, HttpServletResponse response) {
		if (null == J2EEUtil.checkPermit(5, userService, request, response)) return;
		try {
			RobotHelper.getInstance().closeRobot();
			ResponseUtil.render(response, ResponseBean.getSuccessBean());
		} catch (Exception e) {
			ResponseUtil.render(response, ResponseBean.getErrorBean().setInfo(e.getMessage()));
		}
	}
}