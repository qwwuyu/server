package com.qwwuyu.server.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HtmlCtrl {
    @RequestMapping("/robots.txt")
    public String robots(HttpServletRequest request) {
        return "/html/robots.txt";
    }

    @RequestMapping("/")
    public String toWelcome(HttpServletRequest request) {
        if ("/".equals(request.getRequestURI())) {
            return "/html/empty.html";
        } else {
            return "/html/404.html";
        }
    }

    @RequestMapping({"/card", "/note", "/flag",})
    public String toWelcome2(HttpServletRequest request) {
        return "/WEB-INF/jsp/index.jsp";
    }

    @RequestMapping({"/card/send", "/note/send", "/flag/send",})
    public String toSend(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String path = requestURI.substring(requestURI.indexOf("/") + 1, requestURI.indexOf("/send"));
        return "/WEB-INF/jsp/send_" + path + ".jsp";
    }

    @RequestMapping({"/note/content",})
    public String toContent(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String path = requestURI.substring(requestURI.indexOf("/") + 1, requestURI.indexOf("/content"));
        return "/WEB-INF/jsp/content_" + path + ".jsp";
    }

    @RequestMapping("/error500")
    public String to500(HttpServletRequest request) {
        return "/html/500.html";
    }

    @RequestMapping("/error404")
    public String to404(HttpServletRequest request) {
        return "/html/404.html";
    }
}