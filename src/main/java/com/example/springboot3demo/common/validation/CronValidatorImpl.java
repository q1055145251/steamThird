package com.example.springboot3demo.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.scheduling.support.CronExpression;

@SuppressWarnings("unused")
public class CronValidatorImpl implements ConstraintValidator<Cron, String> {
    @Override
    public void initialize(Cron constraintAnnotation) {
        //传入value 值，可以在校验中使用
        String value = constraintAnnotation.value();
    }
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return CronExpression.isValidExpression(value);
    }
}
