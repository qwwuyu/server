package com.qwwuyu.server.utils;

import com.alibaba.fastjson.JSON;
import com.qwwuyu.server.bean.ResponseBean;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {
    public static void render(HttpServletResponse response, ResponseBean bean) {
        response.setContentType("text/plain;charset=utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            response.getWriter().write(JSON.toJSONString(bean));
        } catch (IOException e) {
        }
    }
}
