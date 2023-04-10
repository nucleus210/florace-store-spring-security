package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.SupplierController;
import com.nucleus.floracestore.model.view.SupplierViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SupplierAssembler implements RepresentationModelAssembler<SupplierViewModel, EntityModel<SupplierViewModel>> {

    @Override
    public EntityModel<SupplierViewModel> toModel(SupplierViewModel model) {
        return EntityModel.of(model,
                linkTo(methodOn(SupplierController.class).getSupplierById(model.getSupplierId())).withRel("getSupplierById"),
                linkTo(methodOn(SupplierController.class).deleteSupplierById(model.getSupplierId())).withRel("deleteSupplierById"),
                linkTo(methodOn(SupplierController.class).getAllSuppliers()).withRel("getAllSuppliers"));
    }
}