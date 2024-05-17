package com.example.steamThird.common.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.ANNOTATION_TYPE, METHOD, ElementType.FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CronValidatorImpl.class)//此处指定了注解的实现类
public @interface Cron {

    /**
     * 添加value属性，可以作为校验时的条件,若不需要，可去掉此处定义
     */
    String value() default "";
    String message() default "不正确的Cron表达式";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    /**
     * 定义List，为了让Bean的一个属性上可以添加多套规则
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Cron[] value();
    }


}
