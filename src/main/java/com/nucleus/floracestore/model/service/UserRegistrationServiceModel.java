package com.nucleus.floracestore.model.service;

import com.nucleus.floracestore.model.entity.RoleEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class UserRegistrationServiceModel {

    private String username;
    private String email;
    private String password;
    private boolean setActive;
    private Set<RoleEntity> roles = new HashSet<>();

    public void setActive(boolean b) {
    }
}
