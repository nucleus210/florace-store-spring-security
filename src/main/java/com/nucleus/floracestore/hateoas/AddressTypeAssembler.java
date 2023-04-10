package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.AddressTypeController;
import com.nucleus.floracestore.model.view.AddressTypeViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AddressTypeAssembler implements RepresentationModelAssembler<AddressTypeViewModel, EntityModel<AddressTypeViewModel>> {

    @Override
    public EntityModel<AddressTypeViewModel> toModel(AddressTypeViewModel addressTypeViewModel) {

        return EntityModel.of(addressTypeViewModel,
                linkTo(methodOn(AddressTypeController.class).getAddressTypeById(addressTypeViewModel.getAddressTypeId())).withRel("getAddressTypeById"),
                linkTo(methodOn(AddressTypeController.class).getAddressTypeByName(addressTypeViewModel.getAddressTypeName())).withRel("getAddressTypeByCountryName"),
                linkTo(methodOn(AddressTypeController.class).deleteAddressType(addressTypeViewModel.getAddressTypeId())).withRel("deleteAddressTypeById"),
                linkTo(methodOn(AddressTypeController.class).getAllAddressTypes()).withRel("getAllAddressTypes"));
    }
}