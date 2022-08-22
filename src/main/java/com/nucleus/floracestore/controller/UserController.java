package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.model.dto.UserEditDto;
import com.nucleus.floracestore.model.entity.UserEntity;
import com.nucleus.floracestore.model.service.UserEditServiceModel;
import com.nucleus.floracestore.service.UserService;
import com.nucleus.floracestore.service.impl.MyUserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public UserController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @ModelAttribute("userEditModel")
    public UserEditDto userEditModel() {
        return new UserEditDto();
    }

    @GetMapping("/users/edit")
    public String userEdit(Model model, @AuthenticationPrincipal MyUserPrincipal myUserPrincipal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> userEntity = userService.findByUsername(myUserPrincipal.getUsername());
        if (userEntity.isEmpty()) {
            System.out.println("NuLL");

            return "redirect:/users/login";
        } else {
            System.out.println(userEntity.get().getUsername());

            model.addAttribute("editEditModel", new UserEditDto());
            model.addAttribute("userViewData", userEntity.get());
            return "user-edit";

        }
    }

    @PostMapping("/users/edit")
    public String edit(
            @Valid UserEditDto userModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal MyUserPrincipal myUserPrincipal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userEditModel", userModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userEditModel", bindingResult);
            return "redirect:/users/edit";
        }

        UserEditServiceModel serviceModel =
                modelMapper.map(userModel, UserEditServiceModel.class);
        userService.editUser(serviceModel, myUserPrincipal);
        return "redirect:/home";
    }
}
