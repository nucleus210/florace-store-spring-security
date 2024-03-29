package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.payloads.AuthenticationResponse;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;

import java.util.List;
import java.util.Optional;

public interface UserService {


    AuthenticationResponse loginUser(String username, String password);

    UserServiceModel registerUser(UserRegistrationServiceModel user);
    UserServiceModel registerFacebookUser(UserRegistrationServiceModel user);
    void updateUser(UserServiceModel serviceModel, String username);
    UserServiceModel findById(Long id);
    Optional<UserServiceModel> findBySocialId(Long id);
    UserServiceModel findByUsername(String username);
    UserServiceModel findByEmailAddress(String email);
    List<UserServiceModel> findAll();
    boolean existsByUsername(String username);

    UserServiceModel deleteUser(Long userId);

    void updateUserById(Long userId, UserServiceModel mapToService);
}
