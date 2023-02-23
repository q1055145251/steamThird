package com.example.springboot3demo.common.enumType;

public interface ResultCode {

    Integer SUCCESS=200; //成功

    Integer CREATED=201; //生成了新的资源

    Integer DELETE=204; //删除了资源

    Integer BAD=400; //无效请求

    Integer UN_LOGIN=401; //未登录 Unauthorized

    Integer FID=403; //无权限 Forbidden

    Integer NOT_FOUND=404; //资源不存在 Not Found

    Integer UN_ENTITY=422; //上传的文件无效

    Integer T_REQUESTS=429; //请求频繁

    Integer Error=500; //服务器错误

    Integer UN_SERVICE=503; //服务器维护

}
