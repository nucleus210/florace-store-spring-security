package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    UserEntity findByEmailAddress(String email);

    Optional<UserEntity> findByUsernameIgnoreCase(String username);

    @Modifying
    @Query("update UserEntity u set u.password = :password where u.userId = :userId")
    void updatePassword(@Param("password") String password, @Param("userId") Long userId);
}