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
@Constraint(validatedBy = FutureTimeValidatorImpl.class)//此处指定了注解的实现类
public @interface FutureTime {

    /**
     * 添加value属性，可以作为校验时的条件,若不需要，可去掉此处定义
     */
    int value() default 0;
    String message() default "时间不可小于当前时间";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    /**
     * 定义List，为了让Bean的一个属性上可以添加多套规则
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FutureTime[] value();
    }


}
