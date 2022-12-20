package com.nucleus.floracestore.service.impl;

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
    public Optional<ProfileEntity> getProfileById(Long profileId) {
        return profileRepository.findById(profileId);
    }

    @Override
    public Optional<ProfileEntity> getProfileByUserId(Long userId) {
        return profileRepository.findProfileEntitiesByUser_UserId(userId);
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
        UserServiceModel userServiceModel = userService.findByUsername(userIdentifier).orElseThrow();
        profileServiceModel.setUser(userServiceModel);
    }

    public boolean isOwner(String userName, Long id) {
        Optional<ProfileEntity> profileOpt = profileRepository.
                findById(id);
        Optional<UserServiceModel> caller = userService.
                findByUsername(userName);

        if (profileOpt.isEmpty() || caller.isEmpty()) {
            return false;
        } else {
            ProfileEntity profileEntity = profileOpt.get();

            return isAdmin(caller.get()) ||
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
}
