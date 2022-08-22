package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.model.dto.UserRegistrationDto;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserRegistrationController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserRegistrationController(UserService IUserService,
                                      ModelMapper modelMapper) {
        this.userService = IUserService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("userModel")
    public UserRegistrationDto userModel() {
        return new UserRegistrationDto();
    }

    @GetMapping("/users/register")
    public String registerUser() {
        return "register";
    }

    @PostMapping("/users/register")
    public String register(
            @Valid UserRegistrationDto userModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() || !userModel.getPassword().equals(userModel.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("userModel", userModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userModel", bindingResult);
            return "redirect:/users/register";
        }

        UserRegistrationServiceModel serviceModel =
                modelMapper.map(userModel, UserRegistrationServiceModel.class);
        userService.registerAndLoginUser(serviceModel);
        return "redirect:/home";
    }
}
