package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.ProductRatesController;
import com.nucleus.floracestore.model.view.ProductRatesViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductRatesAssembler implements RepresentationModelAssembler<ProductRatesViewModel, EntityModel<ProductRatesViewModel>> {
    @Override
    public EntityModel<ProductRatesViewModel> toModel(ProductRatesViewModel productModel) {

        return EntityModel.of(productModel, //
                linkTo(methodOn(ProductRatesController.class).getProductRatesById(productModel.getProductRateId())).withSelfRel(),
                linkTo(methodOn(ProductRatesController.class).getProductRateByProductIdAndUsername(productModel.getProductRateId(), productModel.getUsername())).withSelfRel(),
                linkTo(methodOn(ProductRatesController.class).getAllProductRatesByProductId(productModel.getProduct().getProductId())).withRel("rates"));

    }
}