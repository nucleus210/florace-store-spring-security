package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.UserEditServiceModel;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.service.impl.MyUserPrincipal;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel);
    boolean isUserNameFree(String username);
    Optional<UserServiceModel> getUserById(Long id);
    Optional<UserServiceModel> findByUsername(String username);
    UserServiceModel getUserByEmailAddress(String email);
    List<UserServiceModel> getAllUsers();
    void editUser(UserEditServiceModel serviceModel, MyUserPrincipal principal);
    void updateUserPassword(String password, Long userId);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    UserServiceModel register(UserRegistrationServiceModel user);


}
