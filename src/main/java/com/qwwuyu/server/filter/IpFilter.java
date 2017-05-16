package com.qwwuyu.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.qwwuyu.server.utils.J2EEUtil;

public class IpFilter implements Filter {
	private Logger logger;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger = Logger.getLogger(IpFilter.class);
		logger.info("IpFilter:init");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		String address = J2EEUtil.getAddress((HttpServletRequest) request);
		logger.info("IpFilter:doFilter:" + address);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		logger.info("IpFilter:destroy");
	}
}
