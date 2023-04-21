package com.nucleus.floracestore.model.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator
        implements ConstraintValidator<IValidEmail, String> {

    private static final String EMAIL_PATTERN = "([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})";

    @Override
    public void initialize(IValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return (validateEmail(email));
    }

    private boolean validateEmail(String email) {
        if (email != null) {
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } else {
            return false;
        }
    }
}