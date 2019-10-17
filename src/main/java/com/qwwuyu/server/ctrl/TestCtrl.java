package com.qwwuyu.server.ctrl;

import com.alibaba.fastjson.JSON;
import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.utils.CommUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestCtrl {
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public void get(HttpServletRequest request, HttpServletResponse response) {
        post(request, response);
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public void post(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            response.getWriter().write(JSON.toJSONString(new ResponseBean(1, "", map)));
        } catch (IOException ignored) {
        }
    }

    @RequestMapping(value = "/timeout", method = {RequestMethod.POST, RequestMethod.GET})
    public void timeout(HttpServletRequest request, HttpServletResponse response) {
        CommUtil.sleep(100000);
        post(request, response);
    }

    @RequestMapping(value = "/error", method = {RequestMethod.POST, RequestMethod.GET})
    public void error(HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("err");
    }
}
