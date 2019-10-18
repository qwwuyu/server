package com.qwwuyu.server.ctrl.admin;

import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.filter.AuthRequired;
import com.qwwuyu.server.utils.J2EEUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@AuthRequired(permit = 5, toAdmin = true)
@RequestMapping("/ad")
public class AdminCtrl {
    private String hand(String path) {
        return Constant.PREFIX + "admin/" + path;
    }

    @AuthRequired(anth = false)
    @RequestMapping("/login")
    public String toLogin(HttpServletRequest request, Model model) {
        return hand("login.html");
    }

    @RequestMapping("")
    public String toIndex(HttpServletRequest request, Model model) {
        User user = J2EEUtil.getUser(request);
        return hand("ad.html");
    }

    @RequestMapping("/java")
    public String toJava(HttpServletRequest request, Model model) {
        User user = J2EEUtil.getUser(request);
        return hand("java.html");
    }

    @RequestMapping("/robot")
    public String toRobot(HttpServletRequest request, Model model) {
        User user = J2EEUtil.getUser(request);
        return hand("robot.html");
    }

    @RequestMapping("/upload")
    public String toUpload(HttpServletRequest request, Model model) {
        User user = J2EEUtil.getUser(request);
        return hand("upload.html");
    }

    @RequestMapping("/video")
    public String toVideo(HttpServletRequest request, Model model) {
        User user = J2EEUtil.getUser(request);
        return hand("video.html");
    }

    @RequestMapping("/videojs")
    public String toVideojs(HttpServletRequest request, Model model) {
        User user = J2EEUtil.getUser(request);
        return hand("videojs.html");
    }
}