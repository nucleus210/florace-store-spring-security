package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.entity.ProfileEntity;
import com.nucleus.floracestore.model.service.ProfileServiceModel;

import java.util.Optional;

public interface ProfileService {
    Optional<ProfileEntity> getProfileById(Long profileId);

    Optional<ProfileEntity> getProfileByUserId(Long userId);

    void updateProfile(ProfileServiceModel profileServiceModel);

    void deleteProfile(Long profileId);

    void saveProfile(ProfileServiceModel profileServiceModel, String userIdentifier);
}
