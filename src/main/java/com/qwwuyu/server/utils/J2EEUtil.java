package com.qwwuyu.server.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.bean.User;

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

	public static boolean isNull(HttpServletResponse response, String... parame) {
		for (int i = 0; i < parame.length; i++) {
			if (null == parame[i] || "".equals(parame[i])) {
				ResponseUtil.render(response, ResponseBean.getErrorBean().setInfo("参数不正确"));
				return true;
			}
		}
		return false;
	}

	public static boolean renderInfo(HttpServletResponse response, String info) {
		if (info != null) {
			ResponseUtil.render(response, ResponseBean.getErrorBean().setInfo(info));
			return true;
		}
		return false;
	}

	/** BCrypt固定格式加密密码 */
	public static String handPwd(String acc, String pwd) {
		int length = acc.length();
		for (int i = length; i < 22; i++) {
			acc = acc + "0";
		}
		String salt = "$2a$10$" + acc.replaceAll("_", "/");
		return BCrypt.hashpw(pwd, salt);
	}

	/** 获取token */
	public static String getToken(User user) {
		Map<String, Object> map = new HashMap<>();
		map.put("acc", user.getName());
		map.put("nick", user.getNick());
		map.put("auth", user.getAuth());
		map.put("id", user.getId());
		map.put("uuid", UUID.randomUUID());
		return new String(Base64.getEncoder().encode(JSON.toJSONString(map).getBytes(StandardCharsets.UTF_8)));
	}

	public static Map<String, Object> parseToken(String token) {
		return JSON.parseObject(new String(Base64.getDecoder().decode(token.getBytes()), StandardCharsets.UTF_8),
				new TypeReference<Map<String, Object>>() {
				});
	}
}
