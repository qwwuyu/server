package com.qwwuyu.server.ctrl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qwwuyu.server.bean.Note;
import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.service.INoteService;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.ResponseUtil;

@Controller
@RequestMapping("/i/note")
public class NoteCtrl {
	@Resource private IUserService userService;
	@Resource private INoteService service;

	@RequestMapping("/get")
	public void getNote(HttpServletRequest request, HttpServletResponse response) {
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
			page = page > 0 ? page : 1;
		} catch (Exception e) {}
		ResponseUtil.render(response, ResponseBean.getSuccessBean().setData(service.getNote(page)));
	}

	@RequestMapping("/send")
	public void sendCard(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("auth");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		if (J2EEUtil.isNull(response, token, content)) return;
		User user = userService.selectByUser(new User().setToken(token));
		if (user == null) {
			J2EEUtil.renderInfo(response, "请先登录");
			return;
		}
		if (user.getAuth() != 5) {
			J2EEUtil.renderInfo(response, "权限不足");
			return;
		}
		if (title == null || !title.matches(".{1,50}") || !title.matches(".*[\\S]+.*")) {
			J2EEUtil.renderInfo(response, "内容不能为空");
			return;
		}
		Note note = new Note().setUserId(user.getId()).setNick(user.getNick()).setTime(System.currentTimeMillis()).setTitle(title)
				.setContent(content);
		service.insert(note);
		ResponseUtil.render(response, ResponseBean.getSuccessBean());
	}

	@RequestMapping("/rm")
	public void rmCard(HttpServletRequest request, HttpServletResponse response) {
		int note_id = 0;
		try {
			note_id = Integer.parseInt(request.getParameter("id"));
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
		Note note = service.selectByPrimaryKey(note_id);
		if (note == null) {
			J2EEUtil.renderInfo(response, "note不存在");
			return;
		}
		if (user.getAuth() != 5 && note.getUserId() != user.getId()) {
			J2EEUtil.renderInfo(response, "你没有这个权限");
			return;
		}
		service.deleteByPrimaryKey(note_id);
		ResponseUtil.render(response, ResponseBean.getSuccessBean());
	}

	@RequestMapping("/content")
	public void contentCard(HttpServletRequest request, HttpServletResponse response) {
		int note_id = 0;
		try {
			note_id = Integer.parseInt(request.getParameter("id"));
		} catch (Exception e) {
			J2EEUtil.renderInfo(response, "参数不正确");
			return;
		}
		Note note = service.selectByPrimaryKey(note_id);
		if (note == null) {
			J2EEUtil.renderInfo(response, "note不存在");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("note", note);
		map.put("sysTime", System.currentTimeMillis());
		ResponseUtil.render(response, ResponseBean.getSuccessBean().setData(map));
	}
}