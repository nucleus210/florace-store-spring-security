package com.nucleus.floracestore.model.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserNameValidator.class)
public @interface IUniqueUserName {

    String message() default "Username is not unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
