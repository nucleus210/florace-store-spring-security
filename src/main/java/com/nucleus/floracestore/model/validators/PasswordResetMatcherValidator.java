package com.nucleus.floracestore.model.validators;

import com.nucleus.floracestore.model.dto.UserPasswordResetDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordResetMatcherValidator implements ConstraintValidator<IPasswordResetMatcher, Object> {

    @Override
    public void initialize(IPasswordResetMatcher constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        UserPasswordResetDto user = (UserPasswordResetDto) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}