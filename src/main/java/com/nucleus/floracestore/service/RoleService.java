package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.entity.RoleEntity;
import com.nucleus.floracestore.model.enums.UserRoleEnum;

public interface RoleService {
//    RoleEntity getRoleByRole(UserRoleEnum role);
      RoleEntity getByRoleName(String roleName);
}
