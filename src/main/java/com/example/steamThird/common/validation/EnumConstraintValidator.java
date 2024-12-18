package com.example.steamThird.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EnumConstraintValidator implements ConstraintValidator<Enum, String> {
    private String[] verification;

    @Override
    public void initialize(Enum anEnum) {
        verification = anEnum.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (verification == null) {
            return true;
        }
        for (String c : verification) {
            if (value.equals(c)) {
                return true;
            }
        }
        return false;
    }
}