package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("password-reset-repository")
public interface UserPasswordReset extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String emailAddress);
}
