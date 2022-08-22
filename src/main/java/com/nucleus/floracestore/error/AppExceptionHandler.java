package com.nucleus.floracestore.error;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(FileStorageException.class)
    public String handleException(FileStorageException exception, RedirectAttributes redirectAttributes, Model model) {

        model.addAttribute("message", exception.getMsg());
        return "/errors/error";
    }
}