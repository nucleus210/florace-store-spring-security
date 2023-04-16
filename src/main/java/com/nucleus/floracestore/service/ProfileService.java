package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.ProfileServiceModel;

public interface ProfileService {
    ProfileServiceModel getProfileById(Long profileId);

    ProfileServiceModel getProfileByUserId(Long userId);
    ProfileServiceModel getProfileByUsername(String username);

    void updateProfile(ProfileServiceModel profileServiceModel);

    void deleteProfile(Long profileId);

    void saveProfile(ProfileServiceModel profileServiceModel, String userIdentifier);
}
