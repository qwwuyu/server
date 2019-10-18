package com.qwwuyu.server.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class J2EEUtil {
    /** 获取客户ip */
    public static String getRealAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /** 获取访问ip */
    public static String getAddress(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    /* ======================== 返回数据处理 ======================== */
    public static ResponseBean getSuccessBean() {
        return new ResponseBean(Constant.HTTP_SUC, "请求成功", null);
    }

    public static ResponseBean getErrorBean() {
        return new ResponseBean(Constant.HTTP_ERR, "请求失败", null);
    }

    /** 输出JSON数据 */
    public static void render(HttpServletResponse response, ResponseBean bean) {
        response.setContentType("text/plain;charset=utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            response.getWriter().write(JSON.toJSONString(bean));
        } catch (IOException ignored) {
        }
    }

    public static boolean isNull(HttpServletResponse response, String... parame) {
        for (String s : parame) {
            if (null == s || "".equals(s)) {
                render(response, getErrorBean().setInfo("参数不正确"));
                return true;
            }
        }
        return false;
    }

    public static boolean renderInfo(HttpServletResponse response, String info) {
        return renderInfo(response, info, 0);
    }

    public static boolean renderInfo(HttpServletResponse response, String info, int code) {
        if (info != null) {
            if (code > 0) response.setStatus(code);
            render(response, getErrorBean().setInfo(info));
            return true;
        }
        return false;
    }

    /* ======================== other ======================== */

    /** BCrypt固定格式加密密码 */
    public static String handPwd(String acc, String pwd) {
        int length = acc.length();
        StringBuilder accBuilder = new StringBuilder(acc);
        for (int i = length; i < 22; i++) {
            accBuilder.append("0");
        }
        acc = accBuilder.toString();
        String salt = "$2a$10$" + acc.replaceAll("_", "/");
        return BCrypt.hashpw(pwd, salt);
    }

    /** 获取token */
    public static String makeToken(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("acc", user.getName());
        map.put("nick", user.getNick());
        map.put("auth", user.getAuth());
        map.put("id", user.getId());
        map.put("uuid", UUID.randomUUID());
        return new String(Base64.getEncoder().encode(JSON.toJSONString(map).getBytes(StandardCharsets.UTF_8)));
    }

    public static Map<String, Object> parseToken(String token) {
        return JSON.parseObject(new String(Base64.getDecoder().decode(token.getBytes()), StandardCharsets.UTF_8), new TypeReference<Map<String, Object>>() {
        });
    }

    public static String getToken(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (token == null || token.length() == 0) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
        }
        return token;
    }
}
