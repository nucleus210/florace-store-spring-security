package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.ProfileAssembler;
import com.nucleus.floracestore.model.dto.ProfileDto;
import com.nucleus.floracestore.model.service.ProfileServiceModel;
import com.nucleus.floracestore.model.view.ProfileViewModel;
import com.nucleus.floracestore.service.ProfileService;
import com.nucleus.floracestore.service.impl.MyUserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestController
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileAssembler assembler;
    private final ModelMapper modelMapper;

    @Autowired
    public ProfileController(ProfileService profileService, ProfileAssembler assembler, ModelMapper modelMapper) {
        this.profileService = profileService;
        this.assembler = assembler;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("profileModel")
    public ProfileDto profileModel() {
        return new ProfileDto();
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping("/users/{username}/profile")
    public ResponseEntity<EntityModel<ProfileViewModel>> addProfileConfirm(
            @Valid ProfileDto profileModel,
            @AuthenticationPrincipal MyUserPrincipal principal) {
        log.info("Posting profile");
        ProfileServiceModel serviceModel =
                modelMapper.map(profileModel, ProfileServiceModel.class);
        profileService.saveProfile(serviceModel, principal.getUserIdentifier());
       return  ResponseEntity
               .created(linkTo(methodOn(ProfileController.class).addProfileConfirm(profileModel, principal)).toUri())
               .body(assembler.toModel(mapToView(serviceModel)));

    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PostMapping("/users/{username}/profile")
    public ResponseEntity<EntityModel<ProfileViewModel>> addProfileConfirmSec(
            @Valid ProfileDto profileModel) {
        log.info("Posting profile");
        ProfileServiceModel serviceModel =
                modelMapper.map(profileModel, ProfileServiceModel.class);
        profileService.saveProfile(serviceModel, getCurrentLoggedUsername());
        return  ResponseEntity
                .created(linkTo(methodOn(ProfileController.class).addProfileConfirmSec(profileModel)).toUri())
                .body(assembler.toModel(mapToView(serviceModel)));

    }


    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping("/users/{username}/profile/{id}")
    public ResponseEntity<EntityModel<ProfileViewModel>> getProfileById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(profileService.getProfileById(id))));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping("/users/{username}/profile")
    public ResponseEntity<EntityModel<ProfileViewModel>> getProfileByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(profileService.getProfileByUsername(username))));
    }
    @PreAuthorize("@profileServiceImpl.isOwner(#principal.username, #id)")
    @DeleteMapping("/users/{username}/profile/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable String username, @PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(mapToView(profileService.getProfileById(id))));
    }
    private ProfileViewModel mapToView(ProfileServiceModel profile) {
        return modelMapper.map(profile, ProfileViewModel.class);
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }
}
