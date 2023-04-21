package com.nucleus.floracestore.model.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Data
public class UserPasswordForgotDto {
    @Email
    @NotEmpty
    private String email;
}
