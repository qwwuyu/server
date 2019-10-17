package com.qwwuyu.server.ctrl;

import com.qwwuyu.server.bean.Note;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.filter.AuthRequired;
import com.qwwuyu.server.service.INoteService;
import com.qwwuyu.server.utils.J2EEUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/i/note")
public class NoteCtrl {
    @Resource
    private INoteService service;

    @RequestMapping("/get")
    public void getNote(HttpServletRequest request, HttpServletResponse response) {
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
            page = page > 0 ? page : 1;
        } catch (Exception ignored) {
        }
        J2EEUtil.render(response, J2EEUtil.getSuccessBean().setData(service.getNote(page)));
    }

    @AuthRequired(permit = Constant.PERMIT_ADMIN)
    @RequestMapping("/send")
    public void sendCard(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getAttribute(Constant.KEY_USER);
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        if (J2EEUtil.isNull(response, content)) return;
        if (title == null || !title.matches(".{1,50}") || !title.matches(".*[\\S]+.*")) {
            J2EEUtil.renderInfo(response, "内容不能为空");
            return;
        }
        Note note = new Note().setUserId(user.getId()).setNick(user.getNick()).setTime(System.currentTimeMillis()).setTitle(title).setContent(content);
        service.insert(note);
        J2EEUtil.render(response, J2EEUtil.getSuccessBean());
    }

    @AuthRequired(permit = Constant.PERMIT_ADMIN)
    @RequestMapping("/rm")
    public void rmCard(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getAttribute(Constant.KEY_USER);
        int note_id;
        try {
            note_id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            J2EEUtil.renderInfo(response, "参数不正确");
            return;
        }
        service.deleteByPrimaryKey(note_id);
        J2EEUtil.render(response, J2EEUtil.getSuccessBean());
    }

    @RequestMapping("/content")
    public void contentCard(HttpServletRequest request, HttpServletResponse response) {
        int note_id;
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
        J2EEUtil.render(response, J2EEUtil.getSuccessBean().setData(map));
    }
}