package com.nucleus.floracestore.controller;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserLoginController {

    @GetMapping("/users/login")
    public String login(ModelAndView model) {
        model.setViewName("login");
        return "login";
    }

    @PostMapping("/users/login-error")
    public String failedLogin(@ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                                      String userName, RedirectAttributes attribute) {
        attribute.addFlashAttribute("bad_credentials", true);
        attribute.addFlashAttribute("username", userName);
        return "redirect:/users/login";
    }
}

