package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.ContactController;
import com.nucleus.floracestore.model.dto.ContactDto;
import com.nucleus.floracestore.model.view.ContactViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class ContactAssembler implements RepresentationModelAssembler<ContactViewModel, EntityModel<ContactViewModel>> {
    private final ModelMapper modelMapper;

    @Autowired
    public ContactAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EntityModel<ContactViewModel> toModel(ContactViewModel contactViewModel) {

        return EntityModel.of(contactViewModel,
                linkTo(methodOn(ContactController.class).createContact(modelMapper.map(contactViewModel, ContactDto.class))).withRel("createContact"),
                linkTo(methodOn(ContactController.class).deleteContactById(contactViewModel.getContactId())).withRel("deleteContactById"),
                linkTo(methodOn(ContactController.class).getAllContacts()).withRel("getAllContacts"));
    }
}