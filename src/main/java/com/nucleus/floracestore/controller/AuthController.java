package com.nucleus.floracestore.controller;


import com.nucleus.floracestore.error.BadRequestException;
import com.nucleus.floracestore.error.EmailAlreadyExistsException;
import com.nucleus.floracestore.error.UsernameAlreadyExistsException;
import com.nucleus.floracestore.model.dto.UserRegistrationDto;
import com.nucleus.floracestore.model.entity.ProfileEntity;
import com.nucleus.floracestore.model.payloads.*;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.service.RoleService;
import com.nucleus.floracestore.service.impl.FacebookService;
import com.nucleus.floracestore.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

@Slf4j
@RestController
public class AuthController {

    private final UserServiceImpl userService;
    private final FacebookService facebookService;
    private final RoleService roleService;

    @Autowired
    public AuthController(UserServiceImpl userService,
                          FacebookService facebookService,
                          RoleService roleService) {
        this.userService = userService;
        this.facebookService = facebookService;
        this.roleService = roleService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<AuthenticationResponse> authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            log.info("Regular user login {}", authenticationRequest);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION)
                    .body(userService.loginUser(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (
                BadCredentialsException ex) {
            log.info("UNAUTHORIZED login {}", ex);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION)
                .body( userService.refreshToken(request, response));

    }
    @PostMapping("/facebook/signin")
    public ResponseEntity<?> facebookAuth(@Valid @RequestBody FacebookLoginRequest facebookLoginRequest) {
        log.info("facebook login {}", facebookLoginRequest);
        String token = facebookService.loginUser(facebookLoginRequest.getAccessToken());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        log.info("facebook login {}", userRegistrationDto);
        UserRegistrationServiceModel user = UserRegistrationServiceModel
                .builder()
                .username(userRegistrationDto.getUsername())
                .email(userRegistrationDto.getEmail())
                .password(userRegistrationDto.getPassword())
                .roles(Set.of(roleService.getByRoleName("USER")))
                .setActive(true)
                .build();
        UserServiceModel newUser = userService.registerUser(user);

//        String token = userService.loginUser(newUser.getUsername(), newUser.getPassword());

        return ResponseEntity.ok("{Success}");
    }

    @PostMapping(value = "/facebook/register", produces = MediaType.APPLICATION_JSON_VALUE)
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
            userService.registerFacebookUser(user);
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
