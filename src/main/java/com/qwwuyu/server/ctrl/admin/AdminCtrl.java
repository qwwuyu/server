package com.qwwuyu.server.ctrl.admin;

import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.filter.AuthRequired;
import com.qwwuyu.server.utils.J2EEUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@AuthRequired(permit = Constant.PERMIT_ADMIN, toAdmin = true)
@RequestMapping("/ad")
public class AdminCtrl {
    private String hand(String path) {
        return Constant.PREFIX + "admin/" + path;
    }

    @AuthRequired(anth = false)
    @RequestMapping("/login")
    public String toLogin(HttpServletRequest request) {
        return hand("login.html");
    }

    @RequestMapping("")
    public String toIndex(HttpServletRequest request) {
        User user = J2EEUtil.getUser(request);
        return hand("ad.html");
    }

    @RequestMapping("file")
    public String toFileManager(HttpServletRequest request) {
        User user = J2EEUtil.getUser(request);
        return hand("file_manager.html");
    }

    @RequestMapping("/java")
    public String toJava(HttpServletRequest request) {
        User user = J2EEUtil.getUser(request);
        return hand("java.html");
    }

    @RequestMapping("/robot")
    public String toRobot(HttpServletRequest request) {
        User user = J2EEUtil.getUser(request);
        return hand("robot.html");
    }
}