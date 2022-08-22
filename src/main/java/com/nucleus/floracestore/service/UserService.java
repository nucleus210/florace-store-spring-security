package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.entity.UserEntity;
import com.nucleus.floracestore.model.service.UserEditServiceModel;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.service.impl.MyUserPrincipal;

import java.util.Optional;

public interface UserService {
    void initializeUsersAndRoles();

    void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel);

    boolean isUserNameFree(String username);

    UserEntity findByEmailAddress(String email);

    void updatePassword(String password, Long userId);

    Optional<UserEntity> findByUsername(String owner);

    void editUser(UserEditServiceModel serviceModel, MyUserPrincipal principal);
}
