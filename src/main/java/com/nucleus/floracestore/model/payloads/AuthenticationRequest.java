package com.nucleus.floracestore.model.payloads;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class AuthenticationRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}