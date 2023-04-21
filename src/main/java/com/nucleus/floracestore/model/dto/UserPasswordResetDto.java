package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.validators.IPasswordResetMatcher;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
@IPasswordResetMatcher
public class UserPasswordResetDto {
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String confirmPassword;
    @NotNull
    @NotEmpty
    private String token;
}
