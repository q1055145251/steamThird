package com.example.springboot3demo.aspect.limiter;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value= METHOD)
/**
 * 限流器 并发限制数量
 */
public @interface LimiterSem {
    /**
     * 最大并发数 默认10
     */

    int value() default 10;

    /**
     * 等待时间 (秒)
     */
    int time() default 5;
}
