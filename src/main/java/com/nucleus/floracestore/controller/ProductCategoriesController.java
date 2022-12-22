package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.ProductCategoryAssembler;
import com.nucleus.floracestore.model.dto.ProductCategoryDto;
import com.nucleus.floracestore.model.service.ProductCategoryServiceModel;
import com.nucleus.floracestore.model.view.ProductCategoryViewModel;
import com.nucleus.floracestore.service.ProductCategoryService;
import com.nucleus.floracestore.service.ProductSubCategoryService;
import com.nucleus.floracestore.service.UserService;
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
public class ProductCategoriesController {
    private final ProductCategoryService productCategoryService;
    private final ProductSubCategoryService productSubCategoryService;
    private final UserService userService;
    private final ProductCategoryAssembler assembler;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductCategoriesController(ProductCategoryService productCategoryService,
                                       ProductSubCategoryService productSubCategoryService,
                                       ModelMapper modelMapper,
                                       UserService userService,
                                       ProductCategoryAssembler assembler) {
        this.productCategoryService = productCategoryService;
        this.productSubCategoryService = productSubCategoryService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.assembler = assembler;
    }

    @PostMapping("/products-categories")
    public ResponseEntity<EntityModel<ProductCategoryViewModel>> createProductCategory(@RequestBody ProductCategoryDto model) {
        ProductCategoryServiceModel productCategoryServiceModel =
                productCategoryService.createProductCategory(modelMapper.map(model, ProductCategoryServiceModel.class), getCurrentLoggedUsername());
        log.info("ProductCategoryController: created product category with id: " + productCategoryServiceModel.getProductCategoryId());
        return ResponseEntity
                .created(linkTo(methodOn(ProductCategoriesController.class).createProductCategory(model)).toUri())
                .body(assembler.toModel(mapToView(productCategoryServiceModel)));

    }

    @GetMapping("/products-categories/{id}")
    public ResponseEntity<EntityModel<ProductCategoryViewModel>> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(productCategoryService.getProductCategoryById(id))));

    }

    @GetMapping("/products-categories/search/category-name/{categoryName}")
    public ResponseEntity<EntityModel<ProductCategoryViewModel>> getCategoryByCategoryName(@PathVariable String categoryName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(productCategoryService.getProductCategoryByCategoryName(categoryName))));
    }

    @GetMapping(value = "/products-categories")
    public ResponseEntity<CollectionModel<EntityModel<ProductCategoryViewModel>>> getAllProductCategories() {
        List<EntityModel<ProductCategoryViewModel>> productCategories =
                productCategoryService.getAllProductCategories()
                        .stream()
                        .map(entity -> assembler.toModel(mapToView(entity)))
                        .toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(productCategories, linkTo(methodOn(ProductCategoriesController.class)
                        .getAllProductCategories())
                        .withSelfRel()));
    }

    private ProductCategoryViewModel mapToView(ProductCategoryServiceModel model) {
        return modelMapper.map(model, ProductCategoryViewModel.class);
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }
}
