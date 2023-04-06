package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.ProfileEntity;
import com.nucleus.floracestore.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("profile-repository")
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findProfileEntityByUser_UserId(Long userId);

    Optional<ProfileEntity> findProfileEntityByUser_Username(String username);
}
