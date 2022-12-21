package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.entity.RoleEntity;

public interface RoleService {
    RoleEntity getByRoleName(String roleName);
}
