package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.ProductCategoryAssembler;
import com.nucleus.floracestore.model.dto.ProductCategoryDto;
import com.nucleus.floracestore.model.service.ProductCategoryServiceModel;
import com.nucleus.floracestore.model.view.ProductCategoryViewModel;
import com.nucleus.floracestore.service.ProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final ProductCategoryAssembler assembler;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductCategoriesController(ProductCategoryService productCategoryService,
                                       ModelMapper modelMapper,
                                       ProductCategoryAssembler assembler) {
        this.productCategoryService = productCategoryService;
        this.modelMapper = modelMapper;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @PostMapping("/products-categories")
    public ResponseEntity<EntityModel<ProductCategoryViewModel>> createProductCategory(@RequestBody ProductCategoryDto model) {
        ProductCategoryServiceModel productCategoryServiceModel =
                productCategoryService.createProductCategory(modelMapper.map(model, ProductCategoryServiceModel.class));
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/products-categories/{id}")
    public void deleteProductCategory(@PathVariable Long id) {
        productCategoryService.deleteProductCategoryById(id);
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
