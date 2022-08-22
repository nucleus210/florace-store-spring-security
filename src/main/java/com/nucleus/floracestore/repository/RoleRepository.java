package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.RoleEntity;
import com.nucleus.floracestore.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByRole(UserRoleEnum role);
}