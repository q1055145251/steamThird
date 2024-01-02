package com.example.springboot3demo.config.shiro;


import com.example.springboot3demo.common.enumType.ErrorCode;
import com.example.springboot3demo.common.enumType.RedisKey;
import com.example.springboot3demo.exceptionhandler.MessageException;
import com.example.springboot3demo.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * MyRealm 校验配置
 */
@Component
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class MyRealm extends AuthorizingRealm {


    @Resource
    private RedisUtil redisUtil;

    @Override
    //获得自己定义的token
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 获取授权信息
     *
     * @param principalCollection 本金托收
     * @return {@link AuthorizationInfo}
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
//        IAuthUser user = (IAuthUser) principalCollection.iterator().next();//获取登录账号
//        Long id = user.getId();
//        String redisKey = RedisKey.PERMISSION_KEY_PREFIX + id;
//        Map<String, Set<String>> role;
//        if (!redisUtil.hasKey(redisKey)) {//缓存不存在
//            role = new HashMap<>();
//            Set<String> allRoleNamesById = permissionService.getAllRoleCodesById(id);
//            if (allRoleNamesById.isEmpty()) {//如果找不到角色则用户失效
//                redisUtil.del(RedisKey.TOKEN_KEY_PREFIX + user.getToken());
//                throw new MessageException(ErrorCode.UN_LOGIN);
//            }
//            role.put("roleName", allRoleNamesById);//查找用戶角色
//            role.put("rolePermission", permissionService.getAllPermissionById(id));
//            redisUtil.set(redisKey, role, TimeUnit.HOURS.toSeconds(1));//保存权限1小时
//        } else {
//            //noinspection unchecked
//            role = (Map<String, Set<String>>) redisUtil.get(redisKey);
//        }
//        Set<String> roleNames = role.get("roleName");//查询角色
//        Set<String> permission = role.get("rolePermission");//查询权限
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.setRoles(roleNames);
//        info.setStringPermissions(permission);
//        return info;
    }

    //使用自定义token
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
//        JwtToken jwtToken = (JwtToken) authenticationToken;
//        String token = (String) jwtToken.getPrincipal();
//        String redisKey = RedisKey.TOKEN_KEY_PREFIX + token;
//        IAuthUser iAuthUser = (IAuthUser) redisUtil.get(redisKey);
//        if (iAuthUser == null) {
//            return null;//过期 不通过
//        }
//        return new SimpleAuthenticationInfo(iAuthUser, iAuthUser.getId(), getName());
    }

}
