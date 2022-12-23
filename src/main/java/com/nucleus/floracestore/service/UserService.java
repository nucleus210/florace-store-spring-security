package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.UserEditServiceModel;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.service.impl.MyUserPrincipal;

import java.util.List;
import java.util.Optional;

public interface UserService {


    public String loginUser(String username, String password);
    public String registerUser(UserRegistrationServiceModel user);
    public UserServiceModel registerFacebookUser(UserRegistrationServiceModel user);
    public void updateUser(UserEditServiceModel serviceModel, MyUserPrincipal principal);
    public UserServiceModel findById(Long id);
    public Optional<UserServiceModel> findBySocialId(Long id);
    public UserServiceModel findByUsername(String username);
    public UserServiceModel findByEmailAddress(String email);
    public List<UserServiceModel> findAll();
    boolean existsByUsername(String username);

}
