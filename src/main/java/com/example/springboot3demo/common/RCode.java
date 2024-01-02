
package com.example.springboot3demo.common;

import com.alibaba.fastjson2.JSON;
import com.example.springboot3demo.common.enumType.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class RCode extends ResponseEntity<Object> {

    //构造方法
    public RCode(HttpStatus status) {
        super(status);
    }


    public RCode(Integer code, String msg) {
        super(new Message(msg), num2HttpStatus(code));
    }

    public RCode(Integer code, Object msg) {
        super(msg, num2HttpStatus(code));
    }

    //成功方法
    public RCode(Object data) {
        super(data, num2HttpStatus(ResultCode.SUCCESS));
    }

    public RCode(String msg) {
        super(new Message(msg), num2HttpStatus(ResultCode.SUCCESS));
    }

    public static HttpStatus num2HttpStatus(Integer code) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        for (HttpStatus httpStatus : HttpStatus.values()) {
            boolean b = code == httpStatus.value();
            if (b) {
                return httpStatus;
            }
        }
        return status;
    }

    /**
     * 直接输出
     *
     * @param code            状态码
     * @param msg             消息
     * @param servletResponse HttpServletResponse
     */
    public static void populateResponse(Integer code, String msg, HttpServletResponse servletResponse) throws IOException {

        ResponseEntity<Object> responseEntity = new RCode(code, msg);
        for (Map.Entry<String, List<String>> header : responseEntity.getHeaders().entrySet()) {
            String chave = header.getKey();
            for (String valor : header.getValue()) {
                servletResponse.addHeader(chave, valor);
            }
        }
        servletResponse.setContentType("application/json;charset=utf-8");
        servletResponse.setStatus(responseEntity.getStatusCodeValue());
        servletResponse.getWriter().write(JSON.toJSONString(responseEntity.getBody()));
    }


    @Data
    @Schema(description = "通用消息提示")
    public static class Message {

        @JsonInclude(value = JsonInclude.Include.NON_NULL)
        @Schema(description = "消息")
        private Object message;

        //向前端返回的内容
        public Message(Object message) {
            this.message = message;
        }

    }
}