package com.qwwuyu.server.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.utils.J2EEUtil;
import com.qwwuyu.server.utils.ResponseUtil;

public class IpFilter implements Filter {
	private Logger logger;
	private Map<String, Long[]> map = new HashMap<String, Long[]>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger = Logger.getLogger(IpFilter.class);
		logger.info("IpFilter:init");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		String requestURI = ((HttpServletRequest) request).getRequestURI();
		String address = J2EEUtil.getAddress((HttpServletRequest) request);
		if ("/i/register".equals(requestURI)) {
			limit(address + requestURI, request, response, chain, 10L, 86400000L);
		} else if ("/i/login".equals(requestURI)) {
			limit(address + requestURI, request, response, chain, 5L, 60000L);
		} else if ("/i/card/send".equals(requestURI)) {
			limit(address + requestURI, request, response, chain, 10L, 43200000L);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		logger.info("IpFilter:destroy");
	}

	private void limit(String key, ServletRequest request, ServletResponse response, FilterChain chain, long maxNum,
			long maxTime) throws IOException, ServletException {
		if (map.containsKey(key)) {
			Long[] ls = map.get(key);
			long num = ls[0] + 1;
			long time = ls[1];
			if (System.currentTimeMillis() - time > maxTime) {// 时间超出限制
				map.put(key, new Long[] { 1l, System.currentTimeMillis() });
			} else if (num > maxNum) {
				ResponseUtil.render(((HttpServletResponse) response), ResponseBean.getErrorBean().setInfo("请求次数限制"));
				return;
			} else {
				map.put(key, new Long[] { num, time });
			}
		} else {
			map.put(key, new Long[] { 1l, System.currentTimeMillis() });
		}
		chain.doFilter(request, response);
	}
}
