package com.example.springboot3demo.common.enumType;

public interface RedisKey {

    /**
     * 权限
     */
    String PERMISSION_KEY_PREFIX = "permission:";

    /**
     * token
     */
    String TOKEN_KEY_PREFIX = "token:";

    /**
     * 订单当前添加数量(天)
     */
    String ORDER_ADD_COUNT_KEY_PREFIX = "order:addCount:";


}
