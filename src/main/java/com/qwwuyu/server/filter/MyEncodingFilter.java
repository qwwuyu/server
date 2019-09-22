package com.qwwuyu.server.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/** 过时,只需要将tomcat的编码统一设置 */
public class MyEncodingFilter extends OncePerRequestFilter {
    private String encoding;
    private String tomcatEncode = "iso8859-1";

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // --响应乱码解决
        response.setCharacterEncoding(encoding);
        // response.setContentType("text/html;charset=" + encoding);
        // --利用装饰设计模式改变request对象和获取请求参数相关的方法,从而解决请求参数乱码问题
        filterChain.doFilter(new EncodingHttpServletRequest(request, encoding), response);
    }

    private class EncodingHttpServletRequest extends HttpServletRequestWrapper {
        private HttpServletRequest request = null;
        private String encoding;
        private boolean isNotEncode = true;

        public EncodingHttpServletRequest(HttpServletRequest request, String encoding) {
            super(request);
            this.request = request;
            this.encoding = encoding;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            try {
                if (request.getMethod().equalsIgnoreCase("POST")) {
                    request.setCharacterEncoding(encoding);
                    return request.getParameterMap();
                } else if (request.getMethod().equalsIgnoreCase("GET")) {
                    Map<String, String[]> map = request.getParameterMap();
                    if (isNotEncode) {
                        for (Map.Entry<String, String[]> entry : map.entrySet()) {
                            String[] vs = entry.getValue();
                            for (int i = 0; i < vs.length; i++) {
                                vs[i] = new String(vs[i].getBytes(tomcatEncode), encoding);
                            }
                        }
                        isNotEncode = false;
                    }
                    return map;
                } else {
                    return request.getParameterMap();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String[] getParameterValues(String name) {
            return getParameterMap().get(name);
        }

        @Override
        public String getParameter(String name) {
            return getParameterValues(name) == null ? null : getParameterValues(name)[0];
        }
    }
}