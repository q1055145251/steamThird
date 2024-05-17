package com.example.steamThird.config;

import com.example.steamThird.config.shiro.*;
import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;

import java.util.*;


/**
 * shiro配置
 */
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ShiroConfig {

    @Resource
    private MyRealm myRealm;

    @Resource
    @Lazy
    private MyCredentialsMatcher myCredentialsMatcher;


    //创建shiro安全管理器
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DefaultWebSecurityManager getDefaultSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        //配置realm
        securityManager.setAuthenticator(modularRealmAuthenticator());
        myRealm.setCredentialsMatcher(myCredentialsMatcher);
        List<Realm> realms = new ArrayList<>();
        realms.add(myRealm);
        ThreadContext.bind(securityManager);
        securityManager.setRealms(realms);
        return securityManager;
    }

    /**
     * 系统自带的Realm管理，主要针对多realm
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ModularRealmAuthenticator modularRealmAuthenticator() {
        //自己重写的ModularRealmAuthenticator
        UserModularRealmAuthenticator modularRealmAuthenticator = new UserModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }


    @Bean("shiroFilter")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> tokenFilterMap = new HashMap<>();
        //anon jwt自定义过滤器
        tokenFilterMap.put("jwt", new JwtFilter());
        tokenFilterMap.put("doc", new DocFilter());//swagger专用拦截
        filterFactoryBean.setFilters(tokenFilterMap);
        Map<String, String> filterMap = new LinkedHashMap<>();
        //swagger放开拦截
        filterMap.put("/webjars/**", "doc");
        filterMap.put("/v3/api-docs/**", "doc");
        filterMap.put("/doc.html", "doc");
        filterMap.put("/druid", "doc");
        filterMap.put("/druid/**", "doc");
        //登录接口不拦截
        filterMap.put("/test/**", "anon");
        filterMap.put("/login", "anon");
        //下载地址不拦截
        filterMap.put("/file/**", "anon");
        //内部消息接口不拦截
        filterMap.put("/userMsg/innerSend", "anon");
        //设置过滤器
        filterMap.put("/**", "anon");
        filterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return filterFactoryBean;
    }

    // 开启注解代理
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
}
