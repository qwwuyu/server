package com.qwwuyu.server.ctrl;

import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.utils.CommUtil;
import com.qwwuyu.server.utils.J2EEUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HtmlCtrl {
    private String hand(String path) {
        return Constant.PREFIX + "static/" + path;
    }

    @RequestMapping("/")
    public String toWelcome(HttpServletRequest request) {
        if ("/".equals(request.getRequestURI())) {
            String token = J2EEUtil.getToken(request);
            if (CommUtil.isExist(token)) {
                return hand("index.html");
            } else {
                return hand("empty.html");
            }
        } else {
            return hand("404.html");
        }
    }

    @RequestMapping({"/card", "/note", "/flag",})
    public String toWelcome2(HttpServletRequest request) {
        return hand("index.html");
    }

    @RequestMapping({"/card/send", "/note/send", "/flag/send",})
    public String toSend(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String path = requestURI.substring(requestURI.indexOf("/") + 1, requestURI.indexOf("/send"));
        return hand("send_" + path + ".html");
    }

    @RequestMapping({"/note/content",})
    public String toContent(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String path = requestURI.substring(requestURI.indexOf("/") + 1, requestURI.indexOf("/content"));
        return hand("content_" + path + ".html");
    }


    @RequestMapping("/robots.txt")
    public String robots(HttpServletRequest request) {
        return hand("robots.txt");
    }

    @RequestMapping("/error500")
    public String to500(HttpServletRequest request) {
        return hand("500.html");
    }

    @RequestMapping("/error404")
    public String to404(HttpServletRequest request) {
        return hand("404.html");
    }
}