package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.ProductSubCategoriesController;
import com.nucleus.floracestore.model.dto.ProductSubCategoryDto;
import com.nucleus.floracestore.model.view.ProductSubCategoryViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductSubCategoryAssembler implements RepresentationModelAssembler<ProductSubCategoryViewModel, EntityModel<ProductSubCategoryViewModel>> {
    private final ModelMapper modelMapper;

    @Autowired
    public ProductSubCategoryAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EntityModel<ProductSubCategoryViewModel> toModel(ProductSubCategoryViewModel productSubCategoryViewModel) {

        return EntityModel.of(productSubCategoryViewModel,
                linkTo(methodOn(ProductSubCategoriesController.class)
                        .createProductSubCategory(modelMapper
                                .map(productSubCategoryViewModel.getProductSubCategoryId(), ProductSubCategoryDto.class),
                                productSubCategoryViewModel.getProductSubCategoryId())).withSelfRel(),
                linkTo(methodOn(ProductSubCategoriesController.class)
                        .getProductSubCategoryById(productSubCategoryViewModel.getProductSubCategoryId())).withSelfRel(),
                linkTo(methodOn(ProductSubCategoriesController.class)
                        .getProductSubCategoryBySubCategoryName(productSubCategoryViewModel.getProductSubCategoryName())).withSelfRel(),
                linkTo(methodOn(ProductSubCategoriesController.class).getAllProductCategories()).withRel("subCategories"));
    }
}