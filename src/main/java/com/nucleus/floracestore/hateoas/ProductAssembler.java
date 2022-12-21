package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.ProductController;
import com.nucleus.floracestore.model.view.ProductViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductAssembler implements RepresentationModelAssembler<ProductViewModel, EntityModel<ProductViewModel>> {

    @Override
    public EntityModel<ProductViewModel> toModel(ProductViewModel productModel) {

        return EntityModel.of(productModel, //
                linkTo(methodOn(ProductController.class).getProductById(productModel.getProductId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products").withSelfRel());

    }
}