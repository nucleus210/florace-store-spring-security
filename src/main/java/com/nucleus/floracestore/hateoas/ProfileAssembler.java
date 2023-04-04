package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.ProfileController;
import com.nucleus.floracestore.model.view.ProfileViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProfileAssembler implements RepresentationModelAssembler<ProfileViewModel, EntityModel<ProfileViewModel>> {

    @Override
    public EntityModel<ProfileViewModel> toModel(ProfileViewModel profileModel) {

        return EntityModel.of(profileModel,
                linkTo(methodOn(ProfileController.class).getProfileById(profileModel.getProfileId())).withSelfRel(),
                linkTo(methodOn(ProfileController.class).getProfileByUsername(profileModel.getUsername())).withSelfRel(),
                linkTo(methodOn(ProfileController.class).deleteProfile(profileModel.getProfileId())).withSelfRel());

    }
}