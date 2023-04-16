package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.UserAssembler;
import com.nucleus.floracestore.model.dto.UserEditDto;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.model.view.UserViewModel;
import com.nucleus.floracestore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    
    private final UserAssembler assembler;
    @Autowired
    public UserController(ModelMapper modelMapper, UserService userService, UserAssembler assembler) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<EntityModel<UserViewModel>> getUserById(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(userService.findById(userId))));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping("/user-names/{username}")
    public ResponseEntity<EntityModel<UserViewModel>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(userService.findByUsername(username))));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PostMapping("/user-names/{username}")
    public ResponseEntity<EntityModel<UserViewModel>> updateUserByUsername(@PathVariable String username, @RequestBody UserEditDto model) {
        userService.updateUser(mapToService(model), username);
        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).updateUserByUsername(username, model)).toUri())
                .body(assembler.toModel(modelMapper.map(model, UserViewModel.class)));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PostMapping("/{userId}")
    public ResponseEntity<EntityModel<UserViewModel>> updateUserById(@PathVariable Long userId, @RequestBody UserEditDto model) {


        userService.updateUserById(userId, mapToService(model));
        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).updateUserById(userId, model)).toUri())
                .body(assembler.toModel(modelMapper.map(model, UserViewModel.class)));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<EntityModel<UserViewModel>> deleteUserById(@PathVariable Long userId) {
        EntityModel<UserViewModel> userViewModel = assembler.toModel(mapToView(userService.deleteUser(userId)));
        return ResponseEntity.status(HttpStatus.OK).body(userViewModel);
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<UserViewModel>>> getAllUsers() {
        List<EntityModel<UserViewModel>> users = userService.findAll().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()));
    }

    private UserViewModel mapToView(UserServiceModel model) {
        return modelMapper.map(model, UserViewModel.class);
    }

    private UserServiceModel mapToService(UserEditDto model) {
        return modelMapper.map(model, UserServiceModel.class);
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
