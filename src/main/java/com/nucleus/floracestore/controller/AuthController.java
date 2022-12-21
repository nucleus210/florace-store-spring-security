package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.error.BadRequestException;
import com.nucleus.floracestore.error.EmailAlreadyExistsException;
import com.nucleus.floracestore.error.UsernameAlreadyExistsException;
import com.nucleus.floracestore.model.entity.ProfileEntity;
import com.nucleus.floracestore.model.payloads.*;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.service.RoleService;
import com.nucleus.floracestore.service.impl.FacebookService;
import com.nucleus.floracestore.service.impl.UserServiceSocialImpl;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

@Slf4j
@RestController
public class AuthController {

    private final UserServiceSocialImpl userService;
    private final FacebookService facebookService;
    private final RoleService roleService;

    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(UserServiceSocialImpl userService,
                          FacebookService facebookService,
                          RoleService roleService,
                          ModelMapper modelMapper) {
        this.userService = userService;
        this.facebookService = facebookService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthenticationRequest loginRequest) {
        String token = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        log.info("facebook login {}", loginRequest);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @PostMapping("/facebook/signin")
    public ResponseEntity<?> facebookAuth(@Valid @RequestBody FacebookLoginRequest facebookLoginRequest) {
        log.info("facebook login {}", facebookLoginRequest);
        String token = facebookService.loginUser(facebookLoginRequest.getAccessToken());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest payload) {
        log.info("creating user {}", payload.getUsername());

        UserRegistrationServiceModel user = UserRegistrationServiceModel
                .builder()
                .username(payload.getUsername())
                .email(payload.getEmail())
                .password(payload.getPassword())
                .roles(Set.of(roleService.getByRoleName("FACEBOOK_USER")))
                .setActive(true)
                .build();
        ProfileEntity profile = new ProfileEntity();
        profile.setFirstName(payload.getName());

        try {
            userService.registerUser(user);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, "User registered successfully"));
    }
}
