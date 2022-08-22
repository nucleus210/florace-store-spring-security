package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.validators.IPasswordResetMatcher;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
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
