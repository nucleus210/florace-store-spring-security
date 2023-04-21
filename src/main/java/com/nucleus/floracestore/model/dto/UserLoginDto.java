package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.validators.IUniqueUserName;
import com.nucleus.floracestore.model.validators.IValidEmail;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class UserLoginDto {
    @NotBlank
    @Size(min = 4, max = 20)
    @IUniqueUserName
    private String username;
    @IValidEmail
    @NotNull
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 4, max = 20)
    private String password;
}
