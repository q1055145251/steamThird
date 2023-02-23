package com.example.springboot3demo.exceptionhandler;


import com.alibaba.fastjson2.JSONObject;
import com.example.springboot3demo.common.R;
import com.example.springboot3demo.common.enumType.ResultCode;
import com.example.springboot3demo.config.ProjectConfig;
import io.lettuce.core.RedisCommandTimeoutException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings({"JavadocDeclaration", "PlaceholderCountMatchesArgumentCount"})
@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    //指定异常执行方法
    @Value("${spring.profiles.active}")
    private String active;


    /**
     * Violation 校验异常 param
     *
     * @param e 错误信息
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public R error(ConstraintViolationException e) {
        setLog(e);
        return new R(ResultCode.BAD, e.getMessage());
    }

    /**
     * Violation 校验异常 body
     *
     * @param e 错误信息
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R error(MethodArgumentNotValidException e) throws NoSuchFieldException, IllegalAccessException {
        setLog(e);
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        StringBuilder message = new StringBuilder();
        for (ObjectError error : errorList) {
            Object argument = Objects.requireNonNull(error.getArguments())[0];
            Field declaredField = argument.getClass().getDeclaredField("defaultMessage");
            declaredField.setAccessible(true);
            message.append(declaredField.get(argument)).append(": ").append(error.getDefaultMessage()).append(",");
        }
        message.deleteCharAt(message.length() - 1);//删掉最后一个逗号
        return new R(ResultCode.BAD, message.toString());
    }

    /**
     * 数据库唯一键异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public R handler(SQLIntegrityConstraintViolationException e) {
        setLog(e);
        return new R(ResultCode.BAD, "重复提交");
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseBody
    public R handler(DuplicateKeyException e){
        setLog(e);
        return new R(ResultCode.BAD,"重复提交");
    }

    /**
     * 参数类型转换错误
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseBody
    public R parameterTypeException(HttpMessageConversionException e) {
        setLog(e);
        return getResultant(ResultCode.Error,"conversionError",e);
    }

    /**
     * 空异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public R nullException(NullPointerException e) {
        setLog(e);
        return getResultant(ResultCode.Error,"NULL ERROR",e);
    }

    /**
     * 时间转换异常
     */
    @ExceptionHandler(value = ParseException.class)
    @ResponseBody
    public R handler(ParseException e) {
        setLog(e);
        return getResultant(ResultCode.Error,"timeParseError",e);
    }

    @ExceptionHandler(value = RedisCommandTimeoutException.class)
    @ResponseBody
    public R handler(RedisCommandTimeoutException e) {
        log.error("运行时异常：------------------redis服务器连接失败-----------{}", e);
        return new R(ResultCode.Error, "redis服务器连接失败");
    }

    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MessageException.class)
    @ResponseBody
    public R error(MessageException e) {
        if (e.getCode() == 500) {
            setLog("未知的500异常错误~~~~~~~~~~~~~~{}", e);
        } else {
            setLog("自定义异常,信息:{}", e);
        }
        if (e.getMsg() instanceof String) {
            return new R(e.getCode(), e.getMsg().toString());
        } else {
            return new R(e.getCode(), e.getMsg());
        }
    }

    /**
     * 全局未知异常错误
     *
     * @param e 未知异常错误
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e) {
        setLog("未知的500异常错误~~~~~~~~~~~~~~{}", e);
        e.printStackTrace();
        return getResultant(ResultCode.Error,e.getMessage(),e);
    }


    /**
     * 错误日志打印 仅保留细节
     *
     * @param message 前缀
     * @param e       异常
     */
    public void setLog(String message, Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(e.getMessage()).append("\n");
        stringBuffer.append(e);
        if (Objects.nonNull(e.getCause())) {
            stringBuffer.append("\n").append(e.getCause().getMessage());
        }
        for (StackTraceElement stackTraceElement : stackTrace) {
            String className = stackTraceElement.getClassName();
            String fileName = stackTraceElement.getFileName();
            if (className.startsWith("com.laituo.schoolStu") && !className.equals("com.laituo.schoolStu.shiro.ExceptionFilter")
                    && !Objects.requireNonNull(fileName).equals("<generated>")) {//只获取项目中错误的代码
                stringBuffer.append("\n").append(stackTraceElement);
            }
        }
        log.error(message, stringBuffer);
        if (ProjectConfig.isDebug()) {
            e.printStackTrace();
        }

    }

    /**
     * 统一返回结果
     *
     * @param code 状态码
     * @param msg  信息
     * @param e    异常信息
     * @return
     */
    public R getResultant(Integer code, String msg, Exception e) {
        JSONObject result = new JSONObject();
        result.put("message", msg);
        if (active.equals("local") || active.equals("dev")) {
            Optional<StackTraceElement> stackTraceElement = Arrays.stream(e.getStackTrace()).filter(item -> item.getClassName().startsWith("com.laituo.schoolStu") && !item.getClassName().equals("com.laituo.schoolStu.shiro.ExceptionFilter")
                    && !Objects.requireNonNull(item.getFileName()).equals("<generated>")).findFirst();
            stackTraceElement.ifPresent(traceElement -> result.put("lineColumn", traceElement));
//            result.put("infoTranslate", ApiUtils.translate("20210323000738859", e.getMessage(), "zh", "7pP45YX_3cjQHJovLmDk"));//添加第一个翻译结果
        }
        return new R(code, result);
    }

    public void setLog(Exception e) {
        setLog("运行时异常：-------{}", e);
    }

}
