package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.ProfileEntity;
import com.nucleus.floracestore.model.entity.RoleEntity;
import com.nucleus.floracestore.model.entity.StorageEntity;
import com.nucleus.floracestore.model.enums.UserRoleEnum;
import com.nucleus.floracestore.model.service.ProfileServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.repository.ProfileRepository;
import com.nucleus.floracestore.service.ProfileService;
import com.nucleus.floracestore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository, UserService userService, ModelMapper modelMapper) {
        this.profileRepository = profileRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProfileServiceModel getProfileById(Long profileId) {
        ProfileEntity profile = profileRepository.findById(profileId).orElseThrow(() ->
                new QueryRuntimeException("Could not find profile with id " + profileId));
        return mapToService(profile);
    }


    @Override
    public ProfileServiceModel getProfileByUserId(Long userId) {
        ProfileEntity profile = profileRepository.findProfileEntityByUser_UserId(userId).orElseThrow(() ->
                new QueryRuntimeException("Could not find profile with id " + userId));
        return mapToService(profile);
    }

    @Override
    public ProfileServiceModel getProfileByUsername(String username) {
        ProfileEntity profile = profileRepository.findProfileEntityByUser_Username(username).orElseThrow(() ->
                new QueryRuntimeException("Could not find profile with username " + username));
        return mapToService(profile);
    }

    @Override
    public void updateProfile(ProfileServiceModel profileServiceModel) {
        Optional<ProfileEntity> profile = profileRepository.findById(profileServiceModel.getProfileId());
        profile.ifPresent(profileEntity -> {
            profileEntity.setFirstName(profileServiceModel.getFirstName());
            profileEntity.setLastName(profileServiceModel.getLastName());
            profileEntity.setBirthDate(profileServiceModel.getBirthDate());
            profileEntity.setGender(profileServiceModel.getGender());
            profileEntity.setJobTitle(profileServiceModel.getJobTitle());
            profileEntity.setPhoneNumber(profileServiceModel.getPhoneNumber());
            profileEntity.setProfilePhotoUrl(modelMapper.map(profileServiceModel.getProfilePhotoUrl(), StorageEntity.class));
            profileRepository.save(profileEntity);
        });
    }

    @Override
    public void deleteProfile(Long profileId) {
        Optional<ProfileEntity> profile = profileRepository.findById(profileId);
        profile.ifPresent(profileRepository::delete);

    }

    @Override
    public void saveProfile(ProfileServiceModel profileServiceModel, String userIdentifier) {
        UserServiceModel userServiceModel = userService.findByUsername(userIdentifier);
        profileServiceModel.setUser(userServiceModel);
    }

    public boolean isOwner(String userName, Long id) {
        Optional<ProfileEntity> profileOpt = profileRepository.
                findById(id);
        UserServiceModel caller = userService.
                findByUsername(userName);

        if (profileOpt.isEmpty() || caller == null) {
            return false;
        } else {
            ProfileEntity profileEntity = profileOpt.get();

            return isAdmin(caller) ||
                    profileEntity.getUser().getUsername().equals(userName);
        }
    }

    private boolean isAdmin(UserServiceModel user) {
        user.
                getRoles().
                stream().
                map(RoleEntity::getRoleName).
                anyMatch(r -> r.equals("ROLE_" + UserRoleEnum.ADMIN));
        return false;
    }

    private ProfileServiceModel mapToService(ProfileEntity profile) {
        return modelMapper.map(profile, ProfileServiceModel.class);
    }
}
