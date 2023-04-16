package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.PhonePrefixController;
import com.nucleus.floracestore.model.view.PhonePrefixViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PhonePrefixAssembler implements RepresentationModelAssembler<PhonePrefixViewModel, EntityModel<PhonePrefixViewModel>> {

    @Override
    public EntityModel<PhonePrefixViewModel> toModel(PhonePrefixViewModel phonePrefixViewModel) {

        return EntityModel.of(phonePrefixViewModel,
                linkTo(methodOn(PhonePrefixController.class).getPhonePrefixById(phonePrefixViewModel.getPhonePrefixId())).withRel("getPhonePrefixById"),
                linkTo(methodOn(PhonePrefixController.class).getPhonePrefixByCountryName(phonePrefixViewModel.getCountryName())).withRel("getPhonePrefixByPhonePrefixName"),
                linkTo(methodOn(PhonePrefixController.class).getPhonePrefixByPhonePrefix(phonePrefixViewModel.getPrefix())).withRel("getPhonePrefixByPhonePrefix"),
                linkTo(methodOn(PhonePrefixController.class).deletePhonePrefix(phonePrefixViewModel.getPhonePrefixId())).withRel("deletePhonePrefixById"),
                linkTo(methodOn(PhonePrefixController.class).getAllPhonePrefixes()).withRel("getAllPhonePrefixes"));
    }
}