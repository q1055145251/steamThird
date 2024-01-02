package com.example.springboot3demo.common;


import com.alibaba.fastjson2.JSON;
import com.example.springboot3demo.common.enumType.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.IOException;

@Data
@Accessors(chain = true)//链式
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "返回类")
public class R<T> {

    @Schema(description = "状态码")
    private Integer code = 200;

    @Schema(description = "消息", nullable = true)
    private String message;

    @Schema(description = "数据", nullable = true)
    private T data;

    public static R<String> ok() {
        R<String> r = new R<>();
        r.setMessage("success");
        return r;
    }

    public static R<?> ok(String msg) {
        R<?> r = new R<>();
        r.setMessage(msg);
        return r;
    }

    public static <T> R<T> data(T obj) {
        R<T> r = new R<>();
        r.setData(obj);
        return r;
    }


    public static R<?> fail(ErrorCode errorCode) {
        R<?> r = new R<>();
        r.setCode(errorCode.getCode());
        r.setMessage(errorCode.getMsg());
        return r;
    }

    public static R<?> fail(ErrorCode errorCode, String msg) {
        R<?> r = new R<>();
        r.setCode(errorCode.getCode());
        r.setMessage(msg);
        return r;
    }

    public R<T> fail(Integer code, T result) {
        this.setCode(code);
        this.setData(result);
        return this;
    }

    public static R<?> fail(Integer code, String result) {
        R<?> r = new R<>();
        r.setCode(code);
        r.setMessage(result);
        return r;
    }


    /**
     * 直接输出
     *
     * @param code            状态码
     * @param msg             消息
     * @param servletResponse HttpServletResponse
     */
    public static void populateResponse(Integer code, String msg, HttpServletResponse servletResponse) throws IOException {

        servletResponse.setContentType("application/json;charset=utf-8");
        servletResponse.setStatus(200);
        servletResponse.getWriter().write(JSON.toJSONString(R.fail(code, msg)));
    }

}
