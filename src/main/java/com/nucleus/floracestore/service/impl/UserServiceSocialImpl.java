package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.EmailAlreadyExistsException;
import com.nucleus.floracestore.error.UsernameAlreadyExistsException;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.service.RoleService;
import com.nucleus.floracestore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceSocialImpl {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public UserServiceSocialImpl(PasswordEncoder passwordEncoder,
                                 UserService userService,
                                 ModelMapper modelMapper,
                                 RoleService roleService,
                                 AuthenticationManager authenticationManager,
                                 JwtTokenProvider tokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public String loginUser(String username, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generateToken(authentication);
    }

    public UserServiceModel registerUser(UserRegistrationServiceModel user) {
        log.info("registering user {}", user.getUsername());

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            log.warn("username {} already exists.", user.getUsername());
            throw new UsernameAlreadyExistsException(
                    String.format("username %s already exists", user.getUsername()));
        }

        if (userService.existsByEmail(user.getEmail())) {
            log.warn("email {} already exists.", user.getEmail());
            throw new EmailAlreadyExistsException(
                    String.format("email %s already exists", user.getEmail()));
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>() {{
            add(roleService.getByRoleName("FACEBOOK_USER"));
        }});

        return userService.register(user);
    }

    public List<UserServiceModel> findAll() {
        log.info("retrieving all users");
        return userService.getAllUsers();
    }

    public Optional<UserServiceModel> findByUsername(String username) {
        log.info("retrieving user {}", username);
        return userService.findByUsername(username);
    }

    public Optional<UserServiceModel> findById(Long id) {
        log.info("retrieving user {}", id);
        return userService.getUserById(id);
    }
}