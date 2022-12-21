package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.RoleEntity;
import com.nucleus.floracestore.model.entity.UserEntity;
import com.nucleus.floracestore.model.enums.UserRoleEnum;
import com.nucleus.floracestore.model.service.UserEditServiceModel;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.repository.RoleRepository;
import com.nucleus.floracestore.repository.UserRepository;
import com.nucleus.floracestore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MyUserDetailsService myUserDetailsService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           MyUserDetailsService myUserDetailsService,
                           ModelMapper modelMapper) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.myUserDetailsService = myUserDetailsService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel) {

        RoleEntity userRole = roleRepository.findByRoleName(UserRoleEnum.USER.name());

        UserEntity newUser = new UserEntity();
        newUser.setUsername(userRegistrationServiceModel.getUsername());
        newUser.setEmail(userRegistrationServiceModel.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRegistrationServiceModel.getPassword()));
        newUser.setAccountCreatedDate(new Date(System.currentTimeMillis()));
        newUser.setRoles(Set.of(userRole));
        userRepository.findByUsername(userRegistrationServiceModel.getUsername())
                .ifPresent(user1 -> {
                    throw new QueryRuntimeException("User already exist");
                });
        userRepository.save(newUser);
        // Login user
        doAutoLogin(userRegistrationServiceModel);
    }

    private void doAutoLogin(UserRegistrationServiceModel userRegistrationServiceModel) {
        try {
            UserDetails principal = myUserDetailsService.loadUserByUsername(userRegistrationServiceModel.getUsername());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principal.getUsername(),
                    principal.getPassword(),
                    principal.getAuthorities()
            );
            log.debug("Logging in with [{}]", authentication.getPrincipal());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            // logger.error("Failure in autoLogin", e);
        }
    }

    @Override
    public boolean isUserNameFree(String username) {
        return userRepository.findByUsernameIgnoreCase(username).isEmpty();
    }

    @Override
    public UserServiceModel getUserByEmailAddress(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user with email " + email));
        log.info("UserService: found user with email " + email);
        return mapToService(user);
    }

    @Override
    public void updateUserPassword(String password, Long userId) {
        userRepository.updatePassword(password, userId);
    }

    @Override
    public Optional<UserServiceModel> findByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user " + username));
        return Optional.of(mapToService(user));
    }

    @Override
    public void editUser(UserEditServiceModel serviceModel, MyUserPrincipal principal) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(principal.getUsername());
        if (userOpt.isPresent()) {
            UserEntity userEntity = userOpt.get();
            userEntity.setUserId(userOpt.get().getUserId());
            userEntity.setUsername(serviceModel.getUsername());
            userEntity.setEmail(serviceModel.getEmail());
            userEntity.setPassword(userEntity.getPassword());
            userEntity.setRoles(userOpt.get().getRoles());
            userRepository.save(userEntity);
        }
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserServiceModel register(UserRegistrationServiceModel model) {
        UserEntity user = modelMapper.map(model, UserEntity.class);
        log.info("UserService: register user with name " + user.getUsername());
        return mapToService(userRepository.save(user));
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToService).collect(Collectors.toList());
    }

    @Override
    public Optional<UserServiceModel> getUserById(Long id) {
        log.info("UserService: found user with id " + id);
        return Optional.of(modelMapper.map(userRepository.findById(id), UserServiceModel.class));
    }

    private UserServiceModel mapToService(UserEntity entity) {
        return modelMapper.map(entity, UserServiceModel.class);
    }
}
