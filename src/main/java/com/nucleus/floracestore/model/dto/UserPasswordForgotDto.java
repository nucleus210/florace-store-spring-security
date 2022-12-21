package com.nucleus.floracestore.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserPasswordForgotDto {
    @Email
    @NotEmpty
    private String email;
}
