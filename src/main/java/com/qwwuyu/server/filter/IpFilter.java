package com.qwwuyu.server.filter;

import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.ResponseUtil;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IpFilter implements Filter {
    private Logger logger = Logger.getLogger("ip");
    private Map<String, Long[]> map = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("IpFilter:init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        String address = J2EEUtil.getAddress(request);
        String realAddress = J2EEUtil.getRealAddress(request);
        String method = request.getMethod();
        logger.info("requestURI:" + requestURI + " method:" + method + " address:" + address + " realAddress:" + realAddress);
        if ("/i/register".equals(requestURI)) {
            limit(address + requestURI, request, response, chain, 10L, 86400000L);
        } else if ("/i/login".equals(requestURI)) {
            limit(address + requestURI, request, response, chain, 5L, 60000L);
        } else if ("/i/card/send".equals(requestURI)) {
            limit(address + requestURI, request, response, chain, 10L, 43200000L);
        } else if ("/test/upload".equals(requestURI)) {
            String token = request.getParameter("token");
            if (token == null || token.length() == 0) {
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    if ("auth".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
            if (token == null || token.length() == 0) {
                J2EEUtil.renderInfo(response, "请先登录");
                return;
            }
            chain.doFilter(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        logger.info("IpFilter:destroy");
    }

    private void limit(String key, ServletRequest request, ServletResponse response, FilterChain chain, long maxNum, long maxTime) throws IOException, ServletException {
        if (map.containsKey(key)) {
            Long[] ls = map.get(key);
            long num = ls[0] + 1;
            long time = ls[1];
            if (System.currentTimeMillis() - time > maxTime) {// 时间超出限制
                map.put(key, new Long[]{1L, System.currentTimeMillis()});
            } else if (num > maxNum) {
                ResponseUtil.render(((HttpServletResponse) response), ResponseBean.getErrorBean().setInfo("请求次数限制"));
                return;
            } else {
                map.put(key, new Long[]{num, time});
            }
        } else {
            map.put(key, new Long[]{1L, System.currentTimeMillis()});
        }
        chain.doFilter(request, response);
    }
}
