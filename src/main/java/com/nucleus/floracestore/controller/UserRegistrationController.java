package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.model.dto.UserRegistrationDto;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserRegistrationController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserRegistrationController(UserService IUserService,
                                      ModelMapper modelMapper) {
        this.userService = IUserService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto userRegistrationDto) {
        userService.registerAndLoginUser(UserRegistrationServiceModel.builder()
                .username(userRegistrationDto.getUsername())
                .email(userRegistrationDto.getEmail())
                .password(userRegistrationDto.getPassword())
                .build());
        return new ResponseEntity<>("User register successfully!.", HttpStatus.OK);
    }
}
