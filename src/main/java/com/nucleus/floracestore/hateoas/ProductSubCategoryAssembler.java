package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.ProductSubCategoriesController;
import com.nucleus.floracestore.model.view.ProductSubCategoryViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductSubCategoryAssembler implements
        RepresentationModelAssembler<ProductSubCategoryViewModel, EntityModel<ProductSubCategoryViewModel>> {

    @Override
    public EntityModel<ProductSubCategoryViewModel> toModel(ProductSubCategoryViewModel productSubCategoryViewModel) {

        return EntityModel.of(productSubCategoryViewModel,
                linkTo(methodOn(ProductSubCategoriesController.class)
                        .getProductSubCategoryById(productSubCategoryViewModel.getProductSubCategoryId())).withSelfRel(),
                linkTo(methodOn(ProductSubCategoriesController.class)
                        .getProductSubCategoryBySubCategoryName(productSubCategoryViewModel.getProductSubCategoryName())).withSelfRel(),
                linkTo(methodOn(ProductSubCategoriesController.class).getAllProductCategories()).withRel("subCategories"));
    }
}