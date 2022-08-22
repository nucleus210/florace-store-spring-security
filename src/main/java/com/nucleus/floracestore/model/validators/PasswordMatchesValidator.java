package com.nucleus.floracestore.model.validators;

import com.nucleus.floracestore.model.dto.UserRegistrationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<IPasswordMatches, Object> {

    @Override
    public void initialize(IPasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        UserRegistrationDto user = (UserRegistrationDto) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}