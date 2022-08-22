package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.model.entity.RoleEntity;
import com.nucleus.floracestore.model.entity.UserEntity;
import com.nucleus.floracestore.model.enums.UserRoleEnum;
import com.nucleus.floracestore.model.service.UserEditServiceModel;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.repository.RoleRepository;
import com.nucleus.floracestore.repository.UserRepository;
import com.nucleus.floracestore.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MyUserDetailsService myUserDetailsService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           MyUserDetailsService myUserDetailsService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    public void initializeUsersAndRoles() {
        initializeRoles();
        initializeUsers();
    }

    public void initializeUsers() {
        if (userRepository.count() == 0) {
            RoleEntity adminRole = roleRepository.findByRole(UserRoleEnum.ADMIN);
            RoleEntity reader = roleRepository.findByRole(UserRoleEnum.VIEWER);

            UserEntity userJohn = new UserEntity();
            userJohn.setUsername("john");
            userJohn.setEmailAddress("johnlopes@gmail.com");
            userJohn.setPassword(passwordEncoder.encode("test123"));
            userJohn.setAccountCreatedDate(new Date(System.currentTimeMillis()));
            userJohn.setRoles(Set.of(reader));
            userRepository.save(userJohn);

            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setEmailAddress("nucleus210@gmail.com");
            admin.setPassword(passwordEncoder.encode("test123"));
            admin.setAccountCreatedDate(new Date(System.currentTimeMillis()));
            admin.setRoles(Set.of(adminRole, reader));
            userRepository.save(admin);
        }
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            UserRoleEnum[] roles = UserRoleEnum.values().clone();
            for (UserRoleEnum role : roles) {
                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setRole(UserRoleEnum.valueOf(String.valueOf(role)));
                roleRepository.save(roleEntity);
            }
        }
    }

    @Override
    public void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel) {

        RoleEntity userRole = roleRepository.findByRole(UserRoleEnum.VIEWER);

        UserEntity newUser = new UserEntity();

        newUser.setUsername(userRegistrationServiceModel.getUsername());
        newUser.setEmailAddress(userRegistrationServiceModel.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRegistrationServiceModel.getPassword()));
        newUser.setAccountCreatedDate(new Date(System.currentTimeMillis()));
        newUser.setRoles(Set.of(userRole));

        userRepository.save(newUser);

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
            // logger.debug("Logging in with [{}]", authentication.getPrincipal());
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
    public UserEntity findByEmailAddress(String email) {
        return userRepository.findByEmailAddress(email);
    }

    @Override
    public void updatePassword(String password, Long userId) {
        userRepository.updatePassword(password, userId);
    }

    @Override
    public Optional<UserEntity> findByUsername(String owner) {
        return userRepository.findByUsername(owner);
    }

    @Override
    public void editUser(UserEditServiceModel serviceModel, MyUserPrincipal principal) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(principal.getUsername());
        if (userOpt.isPresent()) {
            UserEntity userEntity = userOpt.get();
            userEntity.setUserId(userOpt.get().getUserId());
            userEntity.setUsername(serviceModel.getUsername());
            userEntity.setEmailAddress(serviceModel.getEmailAddress());
            userEntity.setPassword(userEntity.getPassword());
            userEntity.setRoles(userOpt.get().getRoles());
            userRepository.save(userEntity);
        }
    }
}
