package com.nucleus.floracestore.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class UserPasswordForgotDto {
    @Email
    @NotEmpty
    private String email;
}
