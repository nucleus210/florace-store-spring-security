package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.ProductAssembler;
import com.nucleus.floracestore.model.dto.ProductDto;
import com.nucleus.floracestore.model.service.ProductCategoryServiceModel;
import com.nucleus.floracestore.model.service.ProductServiceModel;
import com.nucleus.floracestore.model.service.ProductStatusServiceModel;
import com.nucleus.floracestore.model.service.ProductSubCategoryServiceModel;
import com.nucleus.floracestore.model.view.ProductViewModel;
import com.nucleus.floracestore.service.*;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
public class ProductController {
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final ProductSubCategoryService productSubCategoryService;
    private final ProductStatusService productStatusService;
    private final ProductAssembler assembler;
    private final StorageService storageService;
    private final UserService userService;
    @Autowired
    public ProductController(ModelMapper modelMapper,
                             ProductService productService,
                             ProductCategoryService productCategoryService,
                             ProductSubCategoryService productSubCategoryService,
                             ProductStatusService productStatusService,
                             ProductAssembler assembler,
                             StorageService storageService,
                             UserService userService) {
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.productSubCategoryService = productSubCategoryService;
        this.productStatusService = productStatusService;
        this.assembler = assembler;
        this.storageService = storageService;
        this.userService = userService;
    }

    @PostMapping("/products")
    public ResponseEntity<EntityModel<ProductViewModel>> productCreate(@RequestBody ProductDto model) {
        ProductCategoryServiceModel productCategory =
                productCategoryService.getProductCategoryByCategoryName(model.getProductCategory());
        ProductSubCategoryServiceModel productSubCategory =
                productSubCategoryService.getProductSubCategoryBySubCategoryName(model.getProductSubCategory());
        ProductStatusServiceModel productStatus = productStatusService.getProductStatusByProductStatusName(model.getProductStatus());
        ProductServiceModel productServiceModel = modelMapper.map(model, ProductServiceModel.class);

        productServiceModel.setProductStatus(productStatus);
        productServiceModel.setProductCategory(productCategory);
        productServiceModel.setProductSubCategory(productSubCategory);

        ProductServiceModel product = productService.saveProduct(productServiceModel, getCurrentLoggedUsername());

//        ProductServiceModel product = productService.getByProductName(model.getProductName());
        return ResponseEntity
                .created(linkTo(methodOn(ProductController.class).productCreate(model)).toUri())
                .body(assembler.toModel(mapToView(product)));
    }

    //TODO: implement restful method
    @PatchMapping("/products/{id}")
    public String productEdit(@PathVariable Long id,
                              @Valid ProductDto productModel,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productModel", productModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productModel", bindingResult);
            return "redirect:/edit/{id}";
        }
        ProductServiceModel serviceModel = modelMapper.map(productModel, ProductServiceModel.class);

        productService.updateProduct(serviceModel);
        return "redirect:/details/{id}";
    }

    @GetMapping("/products/search/{productName}")
    public ResponseEntity<EntityModel<ProductViewModel>> getProductByName(@PathVariable String productName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(productService.getByProductName(productName))));
    }
    @GetMapping("/products/search/categories/{categoryName}")
    public ResponseEntity<CollectionModel<EntityModel<ProductViewModel>>> getAllProductsByCategoryName(@PathVariable String categoryName) {
        List<EntityModel<ProductViewModel>> products = productService.getAllProductByCategoryName(categoryName).stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(products, linkTo(methodOn(ProductController.class)
                        .getAllProductsByCategoryName(categoryName)).withSelfRel()));
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<EntityModel<ProductViewModel>> getProductById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(productService.getProductById(id))));
    }

    @GetMapping(value = "/products")
    public ResponseEntity<CollectionModel<EntityModel<ProductViewModel>>> getAllProducts() {
        List<EntityModel<ProductViewModel>> products = productService.getAllProducts().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(products, linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    private ProductViewModel mapToView(ProductServiceModel product) {
        return modelMapper.map(product, ProductViewModel.class);
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }
}
