package com.example.steamThird.common.enumType;

public enum ErrorCode {
    //通用
    COMMON_ID_ERROR(-11, "id有误"),
    COMMON_ILLEGAL_ACCESS(-12, "非法访问"),
    COMMON_PATTERN_ERROR(-13, "时间格式有误"),
    COMMON_RES_REPEAT(-14, "重复提交"),
    COMMON_SERVER_MAX(-15, "服务器爆满了,请稍后再试"),
    COMMON_PAGE_ERROR(-16, "单页最大500条"),





    //系统默认
    FILE_IMG_ERROR(400, "图片解析失败"),
    BAD(400, "请求参数有误"),

    UN_LOGIN(401, "未登录或登录已过期"),

    FID(403, "无权限"),

    NOT_FOUND(404, "资源不存在"),

    UN_ENTITY(422, "上传的文件无效"),

    T_REQUESTS(429, "请求频繁,请稍后重试"),

    Error(500, "服务器错误"),

    UN_SERVICE(503, "服务器维护或繁忙,请稍后重试");


    ErrorCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
