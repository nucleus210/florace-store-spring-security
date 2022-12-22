package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.ProductSubCategoryAssembler;
import com.nucleus.floracestore.model.dto.ProductSubCategoryDto;
import com.nucleus.floracestore.model.service.ProductCategoryServiceModel;
import com.nucleus.floracestore.model.service.ProductSubCategoryServiceModel;
import com.nucleus.floracestore.model.view.ProductSubCategoryViewModel;
import com.nucleus.floracestore.service.ProductCategoryService;
import com.nucleus.floracestore.service.ProductSubCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
public class ProductSubCategoriesController {
    private final ModelMapper modelMapper;
    private final ProductCategoryService productCategoryService;
    private final ProductSubCategoryService productSubCategoryService;
    private final ProductSubCategoryAssembler assembler;

    @Autowired
    public ProductSubCategoriesController(ModelMapper modelMapper,
                                          ProductCategoryService productCategoryService,
                                          ProductSubCategoryService productSubCategoryService,
                                          ProductSubCategoryAssembler assembler) {
        this.modelMapper = modelMapper;
        this.productCategoryService = productCategoryService;
        this.productSubCategoryService = productSubCategoryService;
        this.assembler = assembler;
    }

    @PostMapping("/products-sub-categories")
    public ResponseEntity<EntityModel<ProductSubCategoryViewModel>> createProductSubCategory(@RequestBody ProductSubCategoryDto model,
                                                                                             @PathVariable Long productCategoryId) {
        ProductCategoryServiceModel productCategoryServiceModel =
                productCategoryService.getProductCategoryById(productCategoryId);
        model.setProductCategory(productCategoryServiceModel);
        ProductSubCategoryServiceModel productSubCategoryServiceModel =
                productSubCategoryService.createProductSubCategory(modelMapper.map(model, ProductSubCategoryServiceModel.class),
                        productCategoryId,
                        getCurrentLoggedUsername());
        log.info("ProductSubCategoryController: created product sub category with id: "
                + productSubCategoryServiceModel.getProductSubCategoryId());

        return ResponseEntity
                .created(linkTo(methodOn(ProductSubCategoriesController.class).createProductSubCategory(model, productCategoryId)).toUri())
                .body(assembler.toModel(mapToView(productSubCategoryServiceModel)));
    }

    @GetMapping("/products-sub-categories/{id}")
    public ResponseEntity<EntityModel<ProductSubCategoryViewModel>> getProductSubCategoryById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(productSubCategoryService.getProductSubCategoryById(id))));

    }

    @GetMapping("/products-sub-categories/search/sub-category-name/{subCategoryName}")
    public ResponseEntity<EntityModel<ProductSubCategoryViewModel>> getProductSubCategoryBySubCategoryName(@PathVariable String subCategoryName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(productSubCategoryService.getProductSubCategoryBySubCategoryName(subCategoryName))));
    }

    @GetMapping(value = "/products-sub-categories")
    public ResponseEntity<CollectionModel<EntityModel<ProductSubCategoryViewModel>>> getAllProductCategories() {
        List<EntityModel<ProductSubCategoryViewModel>> productSubCategories =
                productSubCategoryService.getAllSubCategories()
                        .stream()
                        .map(entity -> assembler.toModel(mapToView(entity)))
                        .toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(productSubCategories, linkTo(methodOn(ProductCategoriesController.class)
                        .getAllProductCategories())
                        .withSelfRel()));
    }

    @GetMapping(value = "/products-categories/search/category-name/{categoryName}")
    public ResponseEntity<CollectionModel<EntityModel<ProductSubCategoryViewModel>>> getAllSubCategoriesByCategoryName(@PathVariable String categoryName) {
        List<EntityModel<ProductSubCategoryViewModel>> productSubCategories =
                productSubCategoryService.getAllSubCategoriesByCategoryName(categoryName)
                        .stream()
                        .map(entity -> assembler.toModel(mapToView(entity)))
                        .toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(productSubCategories, linkTo(methodOn(ProductSubCategoriesController.class)
                        .getAllSubCategoriesByCategoryName(categoryName))
                        .withSelfRel()));
    }

    private ProductSubCategoryViewModel mapToView(ProductSubCategoryServiceModel productSubCategoryServiceModel) {
        return modelMapper.map(productSubCategoryServiceModel, ProductSubCategoryViewModel.class);
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }
}
