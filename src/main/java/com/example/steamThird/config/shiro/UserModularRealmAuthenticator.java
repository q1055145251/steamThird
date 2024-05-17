package com.example.steamThird.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Slf4j
public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 强制转换回自定义的CustomizedToken
        JwtToken jwtToken = (JwtToken) authenticationToken;
        // 登录类型
        Set<String> loginType = jwtToken.getLoginType();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        List<Realm> typeRealms = new ArrayList<>();
        for (Realm realm : realms) {
            if (loginType.stream().anyMatch(item->realm.getName().contains(item))){//如果符合条件
                typeRealms.add(realm);
            }
        }
        //判断是单Realm还是多Realm
        if (typeRealms.size() == 1){
//            log.debug("单Realm doSingleRealmAuthentication() execute ");
            return doSingleRealmAuthentication(typeRealms.get(0), jwtToken);
        }
        else{
//            log.debug("多Realm doMultiRealmAuthentication() execute ");
            return doMultiRealmAuthentication(typeRealms, jwtToken);
        }
    }
}