package com.example.springboot3demo.aspect.limiter;


import com.example.springboot3demo.common.enumType.ResultCode;
import com.example.springboot3demo.exceptionhandler.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

@Component
@Order
@Aspect
@Slf4j
public class LimiterSemAspect {

    private static final ConcurrentHashMap<String, Semaphore> map = new ConcurrentHashMap<>();


    @Pointcut("@annotation(com.example.springboot3demo.aspect.limiter.LimiterSem)")
    public void cut() {
    }

    @Around("cut()")
    public Object deal(ProceedingJoinPoint point) throws Throwable {
        final MethodSignature signature = (MethodSignature) point.getSignature();//获取类名
        final LimiterSem annotation = signature.getMethod().getAnnotation(LimiterSem.class);
        final int value = annotation.value();//获取注解参数
        final String name = signature.getMethod().toString();//获取方法名
        Semaphore semaphore;
        if (map.containsKey(name)) {
            semaphore = map.get(name);
        } else {
            semaphore = new Semaphore(value, true);//创建限流器
            map.put(name, semaphore);
        }

        if (!semaphore.tryAcquire(1)) {
            throw new MessageException(ResultCode.T_REQUESTS, "当前访问人数较多，请稍后重试");
        }
        try {
            return point.proceed();
        } finally {
            semaphore.release(1);
        }
    }
}
