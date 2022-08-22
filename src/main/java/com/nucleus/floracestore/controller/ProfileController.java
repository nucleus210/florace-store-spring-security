package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.model.dto.ProfileDto;
import com.nucleus.floracestore.model.service.ProfileServiceModel;
import com.nucleus.floracestore.service.ProfileService;
import com.nucleus.floracestore.service.impl.MyUserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ProfileController {

    private final ProfileService profileService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProfileController(ProfileService profileService, ModelMapper modelMapper) {
        this.profileService = profileService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("userModel")
    public ProfileDto profileModel() {
        return new ProfileDto();
    }

    @GetMapping("/users/profile")
    public ModelAndView createProfile(ModelAndView model) {
        model.addObject("profileModel", profileModel());
        model.setViewName("profile");
        return model;
    }

    @PostMapping("/users/profile")
    public String addProfile(
            @Valid ProfileDto profileModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal MyUserPrincipal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("profileModel", profileModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileModel", bindingResult);
            return "redirect:/users/profile";
        }

        ProfileServiceModel serviceModel =
                modelMapper.map(profileModel, ProfileServiceModel.class);


        profileService.saveProfile(serviceModel, principal.getUserIdentifier());

        return "redirect:/home";
    }

    @PreAuthorize("@profileServiceImpl.isOwner(#principal.username, #id)")
    @DeleteMapping("/users/profile/{id}")
    public String deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return "redirect:/home";
    }
}
