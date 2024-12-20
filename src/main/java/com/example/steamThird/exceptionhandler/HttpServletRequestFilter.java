package com.example.steamThird.exceptionhandler;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WebFilter(filterName = "HttpServletRequestFilter", urlPatterns = "/")
@Order(10000)
public class HttpServletRequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
 
    }
 
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String contentType = servletRequest.getContentType();
        //不是JSON格式的请求直接跳过
        if (contentType == null || !contentType.equals(MediaType.APPLICATION_JSON_VALUE)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        ServletRequest requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
        }
        //获取请求中的流如何，将取出来的字符串，再次转换成流，然后把它放入到新request对象中
        // 在chain.doFiler方法中传递新的request对象
        if (null == requestWrapper) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }
 
    @Override
    public void destroy() {
 
    }
}