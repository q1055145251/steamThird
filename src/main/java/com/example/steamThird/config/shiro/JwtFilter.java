package com.example.steamThird.config.shiro;


import com.example.steamThird.common.R;
import com.example.steamThird.common.enumType.ResultCode;
import com.example.steamThird.utils.StringUtils;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

public class JwtFilter extends AuthenticatingFilter {


    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;//登录
        String jwt = request.getHeader("token");
        if (StringUtils.isEmpty(jwt)) {//如果请求头找不到token则从url寻找
            String[] tokens = request.getParameterMap().get("token");
            if (tokens != null) {
                jwt = tokens[0];
            }
        }
        return new JwtToken(jwt, "MyRealm");
    }


    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("token");
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (StringUtils.isEmpty(token)) {
            String[] tokens = request.getParameterMap().get("token");
            if (tokens != null) {
                token = tokens[0];
            }
            if (StringUtils.isEmpty(token)) {
                R.populateResponse(ResultCode.UN_LOGIN, "身份已过期，请重新登录", response);
                return false;
            }
        }
        return executeLogin(servletRequest, servletResponse);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            R.populateResponse(ResultCode.UN_LOGIN, "身份已过期，请重新登录", response);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
