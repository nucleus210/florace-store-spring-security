package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.ProductStatusController;
import com.nucleus.floracestore.model.view.ProductStatusViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductStatusAssembler implements RepresentationModelAssembler<ProductStatusViewModel, EntityModel<ProductStatusViewModel>> {

    private ModelMapper modelMapper;

    @Override
    public EntityModel<ProductStatusViewModel> toModel(ProductStatusViewModel productStatusViewModel) {

        return EntityModel.of(productStatusViewModel,
//                linkTo(methodOn(ProductStatusController.class).createProductStatus(modelMapper.map(productStatusViewModel, ProductStatusDto.class))).withRel("createProductStatus"),
//                linkTo(methodOn(ProductStatusController.class).updateProductStatus(modelMapper.map(productStatusViewModel, ProductStatusDto.class))).withRel("updateProductStatus"),
                linkTo(methodOn(ProductStatusController.class).deleteProductStatus(productStatusViewModel.getProductStatusId())).withRel("deleteProductStatus"),
                linkTo(methodOn(ProductStatusController.class).getAllProductStatuses()).withRel("getAllProductStatuses"));
    }
}