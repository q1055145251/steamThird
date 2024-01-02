package com.example.springboot3demo.aspect.admin;


import com.example.springboot3demo.utils.ShiroUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@SuppressWarnings("unused")
public class AdminValidatorImpl implements ConstraintValidator<Admin, Object> {


    @Override
    public void initialize(Admin constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value != null) {
            return ShiroUtils.hasRole(ShiroUtils.ROLE_ADMIN);
        }
        return true;
    }
}
