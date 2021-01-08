package com.qwwuyu.server.filter;

import com.qwwuyu.server.utils.J2EEUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class IpInterceptor implements HandlerInterceptor {
    private Logger logger = Logger.getLogger("ip");
    private Map<String, Long[]> map = new HashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String address = J2EEUtil.getAddress(request);
        String realAddress = J2EEUtil.getRealAddress(request);
        String method = request.getMethod();
        logger.info("requestURI:" + requestURI + " method:" + method + " address:" + address + " realAddress:" + realAddress);
        if ("/i/register".equals(requestURI)) {
            return limit(address + requestURI, request, response, 10L, 86400000L);
        } else if ("/i/login".equals(requestURI)) {
            return limit(address + requestURI, request, response, 5L, 60000L);
        } else if ("/i/card/send".equals(requestURI)) {
            return limit(address + requestURI, request, response, 10L, 43200000L);
        }
        return true;
    }

    private boolean limit(String key, ServletRequest request, ServletResponse response, long maxNum, long maxTime) throws IOException, ServletException {
        if (map.containsKey(key)) {
            Long[] ls = map.get(key);
            long num = ls[0] + 1;
            long time = ls[1];
            if (System.currentTimeMillis() - time > maxTime) {// 时间超出限制
                map.put(key, new Long[]{1L, System.currentTimeMillis()});
            } else if (num > maxNum) {
                J2EEUtil.render(((HttpServletResponse) response), J2EEUtil.getErrorBean().setInfo("请求次数限制"));
                return false;
            } else {
                map.put(key, new Long[]{num, time});
            }
        } else {
            map.put(key, new Long[]{1L, System.currentTimeMillis()});
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
