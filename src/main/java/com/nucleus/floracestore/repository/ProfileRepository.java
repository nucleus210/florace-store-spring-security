package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.ProfileEntity;
import com.nucleus.floracestore.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("profileRepository")
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findByUser(UserEntity user);

    Optional<ProfileEntity> findProfileEntitiesByUser_UserId(Long userId);
}
