package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("passwordResetRepository")
public interface IUserPasswordReset extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByemailAddress(String emailAddress);
}
