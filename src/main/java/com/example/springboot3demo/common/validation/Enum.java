package com.example.springboot3demo.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumConstraintValidator.class)
public @interface Enum {
    String[] value();
    String message() default "对应值非枚举字符属性";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}