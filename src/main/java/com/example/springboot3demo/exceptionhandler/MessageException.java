package com.example.springboot3demo.exceptionhandler;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageException extends RuntimeException {
    private Object msg;
    private int code = 500;

    public MessageException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public MessageException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public MessageException(int code, Object msg) {
        super(msg.toString());
        this.msg = msg;
        this.code = code;
    }

    public MessageException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
