package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.model.entity.RoleEntity;
import com.nucleus.floracestore.repository.RoleRepository;
import com.nucleus.floracestore.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleEntity getByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

}
