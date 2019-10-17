package com.qwwuyu.server.ctrl;

import com.qwwuyu.server.bean.Flag;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.filter.AuthRequired;
import com.qwwuyu.server.service.IFlagService;
import com.qwwuyu.server.utils.J2EEUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/i/flag")
public class FlagCtrl {
    @Resource
    private IFlagService service;

    @RequestMapping("/get")
    public void getFlag(HttpServletRequest request, HttpServletResponse response) {
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
            page = page > 0 ? page : 1;
        } catch (Exception ignored) {
        }
        J2EEUtil.render(response, J2EEUtil.getSuccessBean().setData(service.getFlag(page)));
    }

    @AuthRequired(permit = Constant.PERMIT_ADMIN)
    @RequestMapping("/send")
    public void sendFlag(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getAttribute(Constant.KEY_USER);
        String title = request.getParameter("title");
        if (title == null || !title.matches(".{1,50}") || !title.matches(".*[\\S]+.*")) {
            J2EEUtil.renderInfo(response, "内容不能为空");
            return;
        }
        Flag flag = new Flag().setNick(user.getNick()).setTime(System.currentTimeMillis()).setTitle(title).setUserId(user.getId());
        service.insert(flag);
        J2EEUtil.render(response, J2EEUtil.getSuccessBean());
    }
}