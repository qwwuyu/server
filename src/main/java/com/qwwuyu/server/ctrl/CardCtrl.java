package com.qwwuyu.server.ctrl;

import com.qwwuyu.server.bean.Card;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.filter.AuthRequired;
import com.qwwuyu.server.service.ICardService;
import com.qwwuyu.server.utils.J2EEUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/i/card")
public class CardCtrl {
    @Resource
    private ICardService service;

    @RequestMapping("/get")
    public void getCard(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getAttribute(Constant.KEY_USER);
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
            page = page > 0 ? page : 1;
        } catch (Exception ignored) {
        }
        J2EEUtil.render(response, J2EEUtil.getSuccessBean().setData(service.getCard(page)));
    }

    @AuthRequired
    @RequestMapping("/send")
    public void sendCard(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getAttribute(Constant.KEY_USER);
        String title = request.getParameter("title");
        if (title == null || !title.matches(".{1,50}") || !title.matches(".*[\\S]+.*")) {
            J2EEUtil.renderInfo(response, "内容不能为空");
            return;
        }
        Card card = new Card().setNick(user.getNick()).setTime(System.currentTimeMillis()).setTitle(title).setUserId(user.getId());
        service.insert(card);
        J2EEUtil.render(response, J2EEUtil.getSuccessBean());
    }

    @AuthRequired
    @RequestMapping("/rm")
    public void rmCard(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getAttribute(Constant.KEY_USER);
        int card_id;
        try {
            card_id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            J2EEUtil.renderInfo(response, "参数不正确");
            return;
        }
        Card card = service.selectByPrimaryKey(card_id);
        if (card == null) {
            J2EEUtil.renderInfo(response, "card不存在");
            return;
        }
        if (Constant.PERMIT_ADMIN != user.getAuth() && !card.getUserId().equals(user.getId())) {
            J2EEUtil.renderInfo(response, "你没有这个权限");
            return;
        }
        service.deleteByPrimaryKey(card_id);
        J2EEUtil.render(response, J2EEUtil.getSuccessBean());
    }
}