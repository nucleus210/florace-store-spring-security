package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.UserEditServiceModel;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.service.impl.MyUserPrincipal;

import java.util.List;
import java.util.Optional;

public interface UserService {


    String loginUser(String username, String password);
    UserServiceModel registerUser(UserRegistrationServiceModel user);
    UserServiceModel registerFacebookUser(UserRegistrationServiceModel user);
    void updateUser(UserEditServiceModel serviceModel, MyUserPrincipal principal);
    UserServiceModel findById(Long id);
    Optional<UserServiceModel> findBySocialId(Long id);
    UserServiceModel findByUsername(String username);
    UserServiceModel findByEmailAddress(String email);
    List<UserServiceModel> findAll();
    boolean existsByUsername(String username);

}
