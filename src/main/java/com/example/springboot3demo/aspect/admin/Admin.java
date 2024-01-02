package com.example.springboot3demo.aspect.admin;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 在字段上打上注解，代表此字段仅管理员可用
 *
 * @author Administrator
 * @date 2024/01/02
 */
@Target({ElementType.ANNOTATION_TYPE, METHOD, ElementType.FIELD,CONSTRUCTOR,PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = AdminValidatorImpl.class)//此处指定了注解的实现类
public @interface Admin {
    String message() default "无权限";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 定义List，为了让Bean的一个属性上可以添加多套规则
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Admin[] value();
    }


}
