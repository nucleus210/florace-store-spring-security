package com.nucleus.floracestore.model.service;

import com.nucleus.floracestore.model.entity.RoleEntity;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class UserServiceModel {
    private Long userId;
    private String username;
    private String email;
    private String salt;
    private String password;
    private int emailValidation;
    private Date accountCreatedDate;
    private long lockDate;
    private boolean setActive;
    private Set<RoleEntity> roles;
}
