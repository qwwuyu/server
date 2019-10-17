package com.qwwuyu.server.ctrl;

import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.RSAUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/i")
public class AuthCtrl {
    @Resource
    private IUserService userService;

    @RequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        String acc = request.getParameter("acc");
        String pwd = request.getParameter("pwd");
        if (J2EEUtil.isNull(response, acc, pwd)) return;
        if (J2EEUtil.renderInfo(response, check(acc, null, null))) return;
        login(response, acc, pwd);
    }

    @RequestMapping("/register")
    public void register(HttpServletRequest request, HttpServletResponse response) {
        String acc = request.getParameter("acc");
        String nick = request.getParameter("nick").trim().replaceAll("\\s{2,}", " ");
        String pwd = request.getParameter("pwd");
        if (J2EEUtil.isNull(response, acc, nick, pwd)) return;
        try {
            pwd = RSAUtil.getDefault().decrypt(pwd);
        } catch (Exception e) {
            J2EEUtil.renderInfo(response, "服务器解密数据失败");
            return;
        }
        if (J2EEUtil.renderInfo(response, check(acc, nick, pwd))) return;
        if (userService.selectByUser(new User().setName(acc)) != null) {
            J2EEUtil.render(response, J2EEUtil.getErrorBean().setInfo("帐号已存在").setStatu(Constant.HTTP_ACC_EXIST));
            return;
        }
        if (userService.selectByUser(new User().setNick(nick)) != null) {
            J2EEUtil.render(response, J2EEUtil.getErrorBean().setInfo("昵称已存在").setStatu(Constant.HTTP_NIKE_EXIST));
            return;
        }
        User user = new User(null, acc, J2EEUtil.handPwd(acc, pwd), nick, 2, J2EEUtil.getAddress(request), null, null, 0L, 0L);
        userService.insert(user);
        login(response, acc, user.getPwd());
    }

    @RequestMapping("/checkToken")
    public void checkToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter("token");
        if (J2EEUtil.isNull(response, token)) return;
        User user = userService.selectByUser(new User().setToken(token));
        if (null == user) {
            J2EEUtil.render(response, J2EEUtil.getErrorBean().setInfo("帐号在其他地方登录").setStatu(3));
            return;
        } else if (System.currentTimeMillis() - user.getTime() > Constant.expiresValue) {
            J2EEUtil.render(response, J2EEUtil.getErrorBean().setInfo("验证已过期").setStatu(2));
            return;
        }
        user.setTime(System.currentTimeMillis());
        userService.updateByPrimaryKeySelective(user);
        J2EEUtil.render(response, J2EEUtil.getSuccessBean());
    }

    private void login(HttpServletResponse response, String acc, String pwd) {
        User user = userService.selectByUser(new User().setName(acc).setPwd(pwd));
        if (null == user) {
            J2EEUtil.renderInfo(response, "帐号不存在或密码错误");
            return;
        }
        user.setToken(J2EEUtil.getToken(user));
        user.setTime(System.currentTimeMillis());
        userService.updateByPrimaryKeySelective(user);
        J2EEUtil.render(response, J2EEUtil.getSuccessBean().setData(user.getToken()));
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