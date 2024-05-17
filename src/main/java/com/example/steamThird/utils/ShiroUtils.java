package com.example.steamThird.utils;


import org.apache.shiro.SecurityUtils;

public class ShiroUtils {


    public static final String ROLE_ADMIN = "admin";


    /**
     * 获取用户缓存
     *
     * @return {@link IAuthUser}
     */
    public static String getUserCache() {
//        return (IAuthUser) SecurityUtils.getSubject().getPrincipal();
        return null;
    }

    public static boolean hasRole(String roleCode) {
        return SecurityUtils.getSubject().hasRole(roleCode);
    }

    public static boolean hasPre(String preCode) {
        return SecurityUtils.getSubject().isPermitted(preCode);
    }

}
