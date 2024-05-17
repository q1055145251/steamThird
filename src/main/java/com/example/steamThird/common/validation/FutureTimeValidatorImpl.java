package com.example.steamThird.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Date;

@SuppressWarnings("unused")
public class FutureTimeValidatorImpl implements ConstraintValidator<FutureTime, Long> {
    @Override
    public void initialize(FutureTime constraintAnnotation) {
        //传入value 值，可以在校验中使用
        int value = constraintAnnotation.value();
    }

    public boolean isValid(Long time, ConstraintValidatorContext constraintValidatorContext) {
        if (time==null){
            return false;
        }
        //如果时间小于或等于当前时间
        return time > new Date().getTime();
    }
}
