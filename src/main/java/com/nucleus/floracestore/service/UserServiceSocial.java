package com.nucleus.floracestore.service;

public interface UserServiceSocial {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
