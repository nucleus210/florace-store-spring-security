package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.ProductCategoriesController;
import com.nucleus.floracestore.model.view.ProductCategoryViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class ProductCategoryAssembler implements RepresentationModelAssembler<ProductCategoryViewModel, EntityModel<ProductCategoryViewModel>> {

    @Override
    public EntityModel<ProductCategoryViewModel> toModel(ProductCategoryViewModel productCategoryViewModel) {

        return EntityModel.of(productCategoryViewModel,
                linkTo(methodOn(ProductCategoriesController.class).getCategoryById(productCategoryViewModel.getProductCategoryId())).withSelfRel(),
                linkTo(methodOn(ProductCategoriesController.class).getAllProductCategories()).withRel("categories"));
    }
}