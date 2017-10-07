package com.qwwuyu.server.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.bean.Card;
import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.service.ICardService;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.ResponseUtil;

@Controller
@RequestMapping("/i/card")
public class CardCtrl {
	@Resource private IUserService userService;
	@Resource private ICardService service;

	@RequestMapping("/get")
	public void getCard(HttpServletRequest request, HttpServletResponse response) {
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
			page = page > 0 ? page : 1;
		} catch (Exception e) {}
		ResponseUtil.render(response, ResponseBean.getSuccessBean().setData(service.getCard(page)));
	}

	@RequestMapping("/send")
	public void sendCard(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("auth");
		String title = request.getParameter("title");
		if (J2EEUtil.isNull(response, token)) return;
		User user = userService.selectByUser(new User().setToken(token));
		if (user == null) {
			J2EEUtil.renderInfo(response, "请先登录");
			return;
		}
		if (title == null || !title.matches(".{1,50}") || !title.matches(".*[\\S]+.*")) {
			J2EEUtil.renderInfo(response, "内容不能为空");
			return;
		}
		Card card = new Card().setNick(user.getNick()).setTime(System.currentTimeMillis()).setTitle(title).setUserId(user.getId());
		service.insert(card);
		ResponseUtil.render(response, ResponseBean.getSuccessBean());
	}

	@RequestMapping("/rm")
	public void rmCard(HttpServletRequest request, HttpServletResponse response) {
		int card_id = 0;
		try {
			card_id = Integer.parseInt(request.getParameter("id"));
		} catch (Exception e) {
			J2EEUtil.renderInfo(response, "参数不正确");
			return;
		}
		String token = request.getParameter("auth");
		if (J2EEUtil.isNull(response, token)) return;
		User user = userService.selectByUser(new User().setToken(token));
		if (user == null) {
			J2EEUtil.renderInfo(response, "请先登录");
			return;
		}
		Card card = service.selectByPrimaryKey(card_id);
		if (card == null) {
			J2EEUtil.renderInfo(response, "card不存在");
			return;
		}
		if (user.getAuth() != 5 && card.getUserId() != user.getId()) {
			J2EEUtil.renderInfo(response, "你没有这个权限");
			return;
		}
		service.deleteByPrimaryKey(card_id);
		ResponseUtil.render(response, ResponseBean.getSuccessBean());
	}
}