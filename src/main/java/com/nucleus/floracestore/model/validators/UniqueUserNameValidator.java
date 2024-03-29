package com.nucleus.floracestore.model.validators;

import com.nucleus.floracestore.service.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUserNameValidator implements ConstraintValidator<IUniqueUserName, String> {

    private final UserService userService;

    public UniqueUserNameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
        if (userName == null) {
            return true;
        }
        return userService.existsByUsername(userName);
    }
}