package com.example.springboot3demo.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.stereotype.Component;

@Component
public class MyCredentialsMatcher extends SimpleCredentialsMatcher {


    //使用自定义token
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        return true;
    }
}
