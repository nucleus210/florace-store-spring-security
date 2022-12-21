package com.nucleus.floracestore.model.service;

import lombok.Data;

@Data
public class UserLoginServiceModel {
    private String username;
    private String rawPassword;
}