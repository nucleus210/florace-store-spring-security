package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.UserController;
import com.nucleus.floracestore.model.view.UserViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements RepresentationModelAssembler<UserViewModel, EntityModel<UserViewModel>> {

    @Override
    public EntityModel<UserViewModel> toModel(UserViewModel model) {
        return EntityModel.of(model,
                linkTo(methodOn(UserController.class).getUserById(model.getUserId())).withRel("getUserById"),
                linkTo(methodOn(UserController.class).getUserByUsername(model.getUsername())).withRel("getUserByUsername"),
                linkTo(methodOn(UserController.class).deleteUserById(model.getUserId())).withRel("deleteUserById"),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("getAllUsers"));
    }
}