package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.PrivilegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("privilege-repository")
public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Long> {
    PrivilegeEntity findByName(String name);
}
