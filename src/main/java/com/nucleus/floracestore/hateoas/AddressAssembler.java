package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.AddressController;
import com.nucleus.floracestore.model.view.AddressViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AddressAssembler implements RepresentationModelAssembler<AddressViewModel, EntityModel<AddressViewModel>> {

    @Override
    public EntityModel<AddressViewModel> toModel(AddressViewModel addressViewModel) {

        return EntityModel.of(addressViewModel,
                linkTo(methodOn(AddressController.class).getAddressById(addressViewModel.getAddressId())).withRel("getAddressById"),
                linkTo(methodOn(AddressController.class).deleteAddress(addressViewModel.getAddressId())).withRel("deleteAddressById"));
    }
}