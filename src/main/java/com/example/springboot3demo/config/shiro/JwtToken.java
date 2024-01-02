package com.example.springboot3demo.config.shiro;

import lombok.Data;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * shiroToken类
 */
@SuppressWarnings("UnusedAssignment")
@Data
@Component
public class JwtToken implements HostAuthenticationToken, RememberMeAuthenticationToken {
    private String token;
    private char[] password;
    private boolean rememberMe;

    private Set<String> loginType;

    private String host;




    public JwtToken(String token) {
        this.token = token;
    }

    public JwtToken(String token, String... loginType) {
        this.token = token;
        this.loginType=new HashSet<>(Arrays.asList(loginType));
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    public JwtToken() {
        this.rememberMe = false;
    }

    public JwtToken(String token, char[] password) {
        this(token, password, false, null);
    }

    public JwtToken(String token, String password, String loginType) {
        this(token, password != null ? password.toCharArray() : null, false, null);
    }

    public JwtToken(String token, char[] password, String host) {
        this(token, password, false, host);
    }

    public JwtToken(String token, String password, String host, String loginType) {
        this(token, password != null ? password.toCharArray() : null, false, host);
    }

    public JwtToken(String token, char[] password, boolean rememberMe) {
        this(token, password, rememberMe, null);
    }

    public JwtToken(String token, String password, boolean rememberMe) {
        this(token, password != null ? password.toCharArray() : null, rememberMe, null);
    }

    public JwtToken(String token, char[] password, boolean rememberMe, String host) {
        this.rememberMe = false;
        this.token = token;
        this.password = password;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    public JwtToken(String username, String password, boolean rememberMe, String host) {
        this(username, password != null ? password.toCharArray() : null, rememberMe, host);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public String toString() {
        String user;
        try{
            user = "123";
        }catch (Exception e){
            user= "123";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" - ");
        sb.append(user);
        sb.append(", rememberMe=").append(this.rememberMe);
        if (this.host != null) {
            sb.append(" (").append(this.host).append(")");
        }
        return sb.toString();
    }
}
