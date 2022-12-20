package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.ProductReviewsController;
import com.nucleus.floracestore.model.view.ProductReviewsViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class ProductReviewsAssembler implements RepresentationModelAssembler<ProductReviewsViewModel, EntityModel<ProductReviewsViewModel>> {

    @Override
    public EntityModel<ProductReviewsViewModel> toModel(ProductReviewsViewModel productModel) {

        return EntityModel.of(productModel, //
                linkTo(methodOn(ProductReviewsController.class).getProductReviewById(productModel.getProductReviewId())).withSelfRel(),
                linkTo(methodOn(ProductReviewsController.class).getProductReviewByProductIdAndUsername(productModel.getProduct().getProductId(), productModel.getUsername())).withRel("ReviewByProductIdAndUsername") ,
                linkTo(methodOn(ProductReviewsController.class).getAllProductReviewsByProductId(productModel.getProduct().getProductId())).withRel("Reviews"));

    }
}