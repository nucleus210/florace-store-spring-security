package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.ProfileAssembler;
import com.nucleus.floracestore.model.dto.ProfileDto;
import com.nucleus.floracestore.model.service.ProfileServiceModel;
import com.nucleus.floracestore.model.view.ProductViewModel;
import com.nucleus.floracestore.model.view.ProfileViewModel;
import com.nucleus.floracestore.service.ProfileService;
import com.nucleus.floracestore.service.impl.MyUserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
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

    @ModelAttribute("userModel")
    public ProfileDto profileModel() {
        return new ProfileDto();
    }

    @PostMapping("/users/profile")
    public ResponseEntity<EntityModel<ProfileViewModel>> addProfileConfirm(
            @Valid ProfileDto profileModel,
            @AuthenticationPrincipal MyUserPrincipal principal) {

        ProfileServiceModel serviceModel =
                modelMapper.map(profileModel, ProfileServiceModel.class);
        profileService.saveProfile(serviceModel, principal.getUserIdentifier());
       return ResponseEntity
               .created(linkTo(methodOn(ProfileController.class).addProfileConfirm(profileModel, principal)).toUri())
               .body(assembler.toModel(mapToView(serviceModel)));

    }
    @GetMapping("/users/profile/{id}")
    public ResponseEntity<EntityModel<ProfileViewModel>> getProfileById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(profileService.getProfileById(id))));
    }
    @GetMapping("/users/{username}/profile")
    public ResponseEntity<EntityModel<ProfileViewModel>> getProfileByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(profileService.getProfileByUsername(username))));
    }
    @PreAuthorize("@profileServiceImpl.isOwner(#principal.username, #id)")
    @DeleteMapping("/users/profile/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(mapToView(profileService.getProfileById(id))));
    }

    private ProfileViewModel mapToView(ProfileServiceModel profile) {
        return modelMapper.map(profile, ProfileViewModel.class);
    }
}
